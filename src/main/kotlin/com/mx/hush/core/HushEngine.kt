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
        println("Output Unneeded: ${extension.outputUnneeded}")

        setupProject()
        initializeConfigParameters()
    }

    fun analyze() {
        val analyzer = HushDeltaAnalyzer(getVulnerabilities(), getSuppressions(), driver)
        analyzer.printReport(getConfigParameter("outputUnneeded"), getConfigParameter("outputSuggested"))

        if (getConfigParameter("writeSuggested")) {
            analyzer.writeSuggestedSuppressions()
            analyzer.reInitialize(getSuppressions())

            println(green("Suppression file written."))
        }

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
        for (configItem in configParameters.keys) {
            for (parameter in project.properties) {
                val key = parameter.key.toLowerCase()

                if (key == configItem.toLowerCase()) {
                    configParameters[configItem] = true

                    println("$configItem enabled")
                }

                if (key == "no${configItem.toLowerCase()}") {
                    configParameters[configItem] = false

                    println("$configItem disabled")
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