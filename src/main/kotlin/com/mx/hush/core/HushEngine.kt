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

import com.mx.hush.HushExtension
import com.mx.hush.core.drivers.HushDriver
import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.HushVulnerability
import com.mx.hush.core.models.green
import org.gradle.api.Project

class HushEngine(private val project: Project, private val driver: HushDriver) {
    private val configParameters = hashMapOf(
        // Output unneeded suppressions
        "outputUnneeded" to true,
        // Throw if unneeded suppressions are found
        "failOnUnneeded" to true,
        // Output suggested suppressions
        "outputSuggested" to true,
        // Write the suggested suppressions to the suppression file
        "writeSuggested" to false,
    )

    private var extension: HushExtension = project.extensions.create("hush", HushExtension::class.java)

    init {
        project.afterEvaluate {
            initializeConfigParameters()
        }

        setupProject()
    }

    fun analyze() {
        val analyzer = HushDeltaAnalyzer(getVulnerabilities(), getSuppressions(), driver)

        if (getConfigParameter("writeSuggested")) {
            analyzer.writeSuggestedSuppressions()
            analyzer.reInitialize(getSuppressions())

            println(green("Suppression file written."))
        }

        analyzer.printReport(getConfigParameter("outputUnneeded"), getConfigParameter("outputSuggested"))
        analyzer.passOrFail(getConfigParameter("failOnUnneeded"))
    }

    private fun setupProject() {
        driver.setupProject()
    }

    private fun getVulnerabilities(): HashMap<String, HushVulnerability> {
        return driver.getVulnerabilities()
    }

    private fun getSuppressions(): List<HushSuppression> {
        return driver.getSuppressions()!!
    }

    private fun initializeConfigParameters() {
        configParameters["outputUnneeded"] = extension.outputUnneeded
        configParameters["failOnUnneeded"] = extension.failOnUnneeded
        configParameters["outputSuggested"] = extension.outputSuggested
        configParameters["writeSuggested"] = extension.writeSuggested

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

    private fun getConfigParameter(parameter: String): Boolean {
        if (!configParameters.containsKey(parameter)) {
            return false
        }

        return configParameters[parameter] as Boolean
    }
}