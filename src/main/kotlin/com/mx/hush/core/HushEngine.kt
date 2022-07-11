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
package com.mx.hush.core

import com.mx.hush.HushExtension.Companion.getHush
import com.mx.hush.core.drivers.HushVulnerabilityScanDriver
import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.HushVulnerability
import com.mx.hush.core.models.green
import org.gradle.api.Project

class HushEngine(project: Project, private val scanDriver: HushVulnerabilityScanDriver) {
    private var extension = project.getHush()

    fun analyze() {
        extension.gitlabConfiguration.validateConfiguration()
        val analyzer = HushDeltaAnalyzer(getVulnerabilities(), getSuppressions(), scanDriver, extension.gitlabConfiguration)

        if (extension.writeSuggested) {
            analyzer.writeSuggestedSuppressions()
            analyzer.reInitialize(getSuppressions())

            println(green("Suppression file written."))
        }

        analyzer.printReport(extension.outputUnneeded, extension.outputSuggested)
        analyzer.passOrFail(extension.failOnUnneeded)
    }

    fun writeSuggestedSuppressions() {
        val analyzer = HushDeltaAnalyzer(getVulnerabilities(), getSuppressions(), scanDriver, extension.gitlabConfiguration)

        analyzer.writeSuggestedSuppressions()
    }

    fun canWriteSuggestedSuppressions(): Boolean {
        return scanDriver.canWriteSuggestedSuppressions()
    }

    fun getReportFilePath(): String {
        return scanDriver.getReportFilePath()
    }

    fun getSuppressionFilePath(): String {
        return scanDriver.getSuppressionFilePath()
    }

    fun setupProject() {
        scanDriver.setupProject()
    }

    private fun getVulnerabilities(): HashMap<String, HushVulnerability> {
        return scanDriver.getVulnerabilities()
    }

    private fun getSuppressions(): List<HushSuppression> {
        return scanDriver.getSuppressions()!!
    }
}