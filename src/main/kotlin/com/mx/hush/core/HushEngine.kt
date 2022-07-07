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

import com.mx.hush.HushExtension.Companion.hush
import com.mx.hush.core.drivers.HushVulnerabilityScanDriver
import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.HushVulnerability
import com.mx.hush.core.models.green
import com.mx.hush.core.models.red
import org.gradle.api.Project

class HushEngine(private val project: Project, private val scanDriver: HushVulnerabilityScanDriver) {
    private val configParameters = hashMapOf(
        // Output unneeded suppressions
        "outputUnneeded" to true,
        // Throw if unneeded suppressions are found
        "failOnUnneeded" to true,
        // Output suggested suppressions
        "outputSuggested" to true,
        // Write the suggested suppressions to the suppression file
        "writeSuggested" to false,
        // Use the Gitlab configuration
        "gitlab" to false,
    )

    private var extension = project.hush()
    private var gitlabConfiguration = extension.gitlabConfiguration

    init {
        project.afterEvaluate {
            initializeConfigParameters()
        }

        setupProject()
    }

    fun analyze() {
        extension.gitlabConfiguration.validateConfiguration()
        val analyzer = HushDeltaAnalyzer(getVulnerabilities(), getSuppressions(), scanDriver, extension.gitlabConfiguration)

        if (getConfigParameter("writeSuggested")) {
            analyzer.writeSuggestedSuppressions()
            analyzer.reInitialize(getSuppressions())

            println(green("Suppression file written."))
        }

        analyzer.printReport(getConfigParameter("outputUnneeded"), getConfigParameter("outputSuggested"))
        analyzer.passOrFail(getConfigParameter("failOnUnneeded"))
    }

    fun getGitlabTokenUrl(): String {
        return "${getGitlabUrl()}/-/profile/personal_access_tokens"
    }

    fun getGitlabUrl(): String {
        return extension.gitlabConfiguration.url
    }

    private fun setupProject() {
        scanDriver.setupProject()
    }

    private fun getVulnerabilities(): HashMap<String, HushVulnerability> {
        return scanDriver.getVulnerabilities()
    }

    private fun getSuppressions(): List<HushSuppression> {
        return scanDriver.getSuppressions()!!
    }

    private fun initializeConfigParameters() {
        initalizeBaseConfigParameters()
        initalizeGitlabConfigParameters()
    }

    private fun initalizeBaseConfigParameters() {
        configParameters["outputUnneeded"] = extension.outputUnneeded
        configParameters["failOnUnneeded"] = extension.failOnUnneeded
        configParameters["outputSuggested"] = extension.outputSuggested
        configParameters["writeSuggested"] = extension.writeSuggested
        configParameters["gitlab"] = extension.gitlabConfiguration.enabled

        for (configItem in configParameters.keys) {
            for (parameter in project.properties) {
                val key = parameter.key.toLowerCase()

                if (key == configItem.toLowerCase()) {
                    configParameters[configItem] = true

                    println("$configItem enabled via parameter")
                }

                if (key == "no${configItem.toLowerCase()}") {
                    configParameters[configItem] = false

                    println("$configItem disabled via parameter")
                }
            }
        }
    }

    private fun initalizeGitlabConfigParameters() {
        gitlabConfiguration = extension.gitlabConfiguration

        for (parameter in project.properties) {
            val key = parameter.key.toLowerCase()

            if (key.contains(".")) {
                val keys = key.split(".")

                if (keys[0] == "gitlabconfiguration") {
                    when (keys[1]) {
                        "url" -> {
                            extension.gitlabConfiguration.url = parameter.value.toString()
                            println("Gitlab host set to ${parameter.value.toString()} via parameter")
                        }

                        "token" -> {
                            extension.gitlabConfiguration.token = parameter.value.toString()
                            println("Gitlab token set via parameter")
                        }

                        "populatenotesonmatch" -> {
                            extension.gitlabConfiguration.populateNotesOnMatch = parameter.value.toString().toBoolean()
                            println("Gitlab populateNotesOnMatch set to ${parameter.value.toString().toBoolean()} via parameter")
                        }

                        else -> {
                            println(red("Unknown Gitlab configuration property '${keys[1]}'"))
                        }
                    }
                }
            }
        }

        extension.gitlabConfiguration.enabled = configParameters["gitlab"] == true
    }

    private fun getConfigParameter(parameter: String): Boolean {
        if (!configParameters.containsKey(parameter)) {
            return false
        }

        return configParameters[parameter] as Boolean
    }
}