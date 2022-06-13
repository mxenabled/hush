package com.mx.hush.core

import com.mx.hush.core.drivers.HushDriver
import com.mx.hush.core.exceptions.HushValidationViolation
import com.mx.hush.core.models.*

class HushDeltaAnalyzer(private val vulnerabilities: HashMap<String, HushVulnerability>, private var suppressions: List<HushSuppression>, private val driver: HushDriver) {
    private var neededSuppressions = mutableListOf<HushSuppression>()
    private var unneededSuppressions = mutableListOf<HushSuppression>()

    init {
        populateNeededSuppressions()
        populateUnneededSuppressions()
    }

    fun reInitialize(suppressions: List<HushSuppression>) {
        neededSuppressions = mutableListOf()
        unneededSuppressions = mutableListOf()
        this.suppressions = suppressions
        populateNeededSuppressions()
        populateUnneededSuppressions()
    }

    fun passOrFail(failOnUnneeded: Boolean) {
        if (failOnUnneeded && neededSuppressions.isNotEmpty() && neededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Vulnerabilities and unneeded suppressions detected. Please see report for details."))
        }

        if (neededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Vulnerabilities detected. Please see report for details."))
        }

        if (failOnUnneeded && neededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Unneeded suppressions detected. Please see report for details."))
        }
    }

    fun printReport(outputUnneeded: Boolean, outputSuggested: Boolean) {
        if (neededSuppressions.isNotEmpty()) {
            val vulnVerbiage = if (neededSuppressions.size > 1) "vulnerabilities" else "vulnerability"

            println("${yellow(neededSuppressions.size.toString())} ${red("$vulnVerbiage found!")}")

            for (suppression in neededSuppressions) {
                println("   - ${suppression.cve} (${suppression.referenceUrl} ${suppression.description})")
            }

            println("\n")
        }

        if (unneededSuppressions.isNotEmpty() && outputUnneeded) {
            val supVerbiage = if (unneededSuppressions.size > 1) "suppressions" else "suppression"

            println("${yellow(unneededSuppressions.size.toString())} ${red("unnecessary $supVerbiage found!")}")

            for (suppression in unneededSuppressions) {
                println("   - ${suppression.cve}")
            }

            println("\n")
        }

        if (!isSuppressionFileValid() && outputSuggested) {
            println(cyan("Suggested suppressions:\n"))
            println(getSuggestedSuppressionText())

            return
        }

        println(green("Project passed validation."))
    }

    fun outputSuggestedSuppressions() {
        val suggested = getSuggestedSuppressionText()

        driver.outputSuggestedSuppressions(suggested)
    }

    private fun isSuppressionFileValid(): Boolean {
        return neededSuppressions.isEmpty() && unneededSuppressions.isEmpty()
    }

    private fun populateNeededSuppressions() {
        for (vulnerability in vulnerabilities) {
            val suppression = suppressions.find { vulnerability.value.cve == it.cve }

            if (suppression == null) {
                val neededSuppression = HushSuppression(vulnerability.value.cve, "Hush generated suppression.")
                neededSuppression.description = vulnerability.value.description
                neededSuppression.referenceUrl = vulnerability.value.referenceUrl

                neededSuppressions.add(neededSuppression)
            }
        }
    }

    private fun populateUnneededSuppressions() {
        for (suppression in suppressions) {
            if (!vulnerabilities.containsKey(suppression.cve)) {
                var cve = suppression.cve

                if (cve == null) {
                    cve = "CVE not set."
                }

                val unneededSuppression = HushSuppression(cve, suppression.notes)

                unneededSuppressions.add(unneededSuppression)
            }
        }
    }

    private fun getSuggestedSuppressionText(): String {
        val suggested = mutableListOf<HushSuppression>()

        if (vulnerabilities.isNotEmpty()) {
            for (vulnerability in vulnerabilities) {
                var notes = "Hush generated suppression."

                if (suppressions.isNotEmpty()) {
                    val existingSuppression = suppressions.find { suppression -> suppression.cve == vulnerability.key }

                    if (existingSuppression?.notes != null) {
                        notes = existingSuppression.notes.toString()
                    }
                }

                val sup = HushSuppression(vulnerability.key, notes)

                suggested.add(sup)
            }
        }

        return driver.getSuggestedSuppressionText(suggested)
    }
}