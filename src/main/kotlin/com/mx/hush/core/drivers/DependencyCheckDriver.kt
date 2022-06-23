/**
 * Copyright 2020 MX Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.hush.core.drivers

import com.google.gson.Gson
import com.mx.hush.core.models.*
import org.gradle.api.Project
import org.gradle.internal.impldep.jakarta.xml.bind.Marshaller
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension
import org.owasp.dependencycheck.gradle.tasks.Analyze
import org.owasp.dependencycheck.reporting.ReportGenerator
import java.io.File
import java.io.FileNotFoundException
import java.io.StringWriter
import java.util.*
import javax.xml.bind.JAXBContext
import kotlin.collections.HashMap

/**
 * Driver for leveraging the dependencyCheckAnalyze plugin
 */
class DependencyCheckDriver(private val project: Project) : HushDriver(project) {
    /**
     * Initialize the dependencyCheck extension, and the dependencyCheckAnalyze task. Configure for JSON output,
     * without suppressions. Configure Hush to run after this task is complete, so it can analyze vulnerabilities
     * and suppressions.
     */
    override fun setupProject() {
        project.afterEvaluate {
            // Create new dependencyCheck extension, ignoring errors from the extension already existing
            try {
                project.extensions.create("dependencyCheck", DependencyCheckExtension::class.java)
            } catch (ignored: Exception) {

            }

            // Register the task, ignoring errors from the task already being registered
            try {
                project.tasks.register("dependencyCheckAnalyze", Analyze::class.java)
            } catch (ignored: Exception) {

            }

            // Make sure the extension gets configured just before the dependencyCheckAnalyze task runs
            project.tasks.named("dependencyCheckAnalyze")
                .get()
                .doFirst {
                    val dependencyCheckExtension = project.extensions.getByName("dependencyCheck") as DependencyCheckExtension

                    dependencyCheckExtension.data.directory = "${project.projectDir}/.dependency-check-data"
                    dependencyCheckExtension.cveValidForHours = 24
                    dependencyCheckExtension.failBuildOnCVSS = 11F
                    dependencyCheckExtension.format = ReportGenerator.Format.JSON
                    dependencyCheckExtension.skipConfigurations.clear()
                    dependencyCheckExtension.skipConfigurations.addAll(
                        Arrays.asList("checkstyle", "detekt", "detektPlugins",
                            "pmd", "spotbugs", "spotbugsPlugins", "spotbugsSlf4j"))
                    dependencyCheckExtension.suppressionFile = null
                    dependencyCheckExtension.outputDirectory = "${project.buildDir}/reports/hush/report.json"
                    dependencyCheckExtension.showSummary = false
                }

            project.tasks.named("hushReport")
                .get()
                .dependsOn(project.tasks.named("dependencyCheckAnalyze"))
        }
    }

    /**
     * Get vulnerabilities from the dependencyCheckAnalyze task's output file and map to HushVulnerability
     */
    override fun getVulnerabilities(): HashMap<String, HushVulnerability> {
        val scanReport = getReport()
        val vulnerabilities = HashMap<String, HushVulnerability>()

        if (scanReport == null) {
            return vulnerabilities
        }

        for (vulnerability in scanReport.getVulnerabilities()) {
            val mappedVulnerability = HushVulnerability(vulnerability.key, vulnerability.value.description, vulnerability.value.references[0].url)
            vulnerabilities[vulnerability.key] = mappedVulnerability
        }

        return vulnerabilities
    }

    /**
     * Get the suppressions as per dependencyCheckAnalyze task's expectations
     */
    override fun getSuppressions(): List<HushSuppression>? {
        if (!File("./dependency_suppression.xml").exists()) {
            return null
        }

        val xmlContents = readFileDirectlyAsText("${project.projectDir}/dependency_suppression.xml")
        val jaxbContext = JAXBContext.newInstance(DependencyCheckScanSuppressions::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val dependencyCheckSuppressions: DependencyCheckScanSuppressions
        val suppressList = mutableListOf<HushSuppression>()

        xmlContents.reader().use {
            dependencyCheckSuppressions = unmarshaller.unmarshal(it) as DependencyCheckScanSuppressions
        }

        for (suppression in dependencyCheckSuppressions.suppress) {
            val hushSuppression = HushSuppression(suppression.cve, suppression.notes)
            suppressList.add(hushSuppression)
        }

        return Collections.unmodifiableList(suppressList)
    }

    /**
     * Generate XML from suggested suppressions, as per the dependencyCheckAnalyze task's expectations
     */
    override fun getSuggestedSuppressionText(suppressions: List<HushSuppression>): String {
        val suggested = DependencyCheckScanSuppressions()

        for(suppression in suppressions) {
            val mappedSuppression = DependencyCheckScanSuppression()
            mappedSuppression.cve = suppression.cve
            mappedSuppression.notes = suppression.notes.toString()
            suggested.suppress.add(mappedSuppression)
        }

        val jaxbContext = JAXBContext.newInstance(DependencyCheckScanSuppressions::class.java)
        val marshaller = jaxbContext.createMarshaller()
        val stringWriter = StringWriter()

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

        stringWriter.use {
            marshaller.marshal(suggested, stringWriter)
        }

        return stringWriter.toString()
    }

    /**
     * Write the suggested suppressions to dependency_suppression.xml, which dependencyCheckAnalyze expects
     */
    override fun writeSuggestedSuppressions(suppressions: String) {
        File("./dependency_suppression.xml").writeText(suppressions)
    }

    private fun getReport(): DependencyCheckScanReport? {
        return try {
            Gson().fromJson(readFileDirectlyAsText("${project.buildDir}/reports/hush/report.json"), DependencyCheckScanReport::class.java)
        } catch (ignored: FileNotFoundException) {
            null
        }
    }

    private fun readFileDirectlyAsText(fileName: String): String
            = File(fileName).readText(Charsets.UTF_8)
}