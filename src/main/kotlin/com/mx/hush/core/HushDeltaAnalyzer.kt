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

import com.mx.hush.GitlabConfiguration
import com.mx.hush.core.drivers.GitlabIssueSearchDriver
import com.mx.hush.core.drivers.HushVulnerabilityScanDriver
import com.mx.hush.core.exceptions.HushValidationViolation
import com.mx.hush.core.models.*

class HushDeltaAnalyzer(private val vulnerabilities: HashMap<String, HushVulnerability>, private var suppressions: List<HushSuppression>, private val scanDriver: HushVulnerabilityScanDriver, private val gitlabConfig: GitlabConfiguration) {
    private var neededSuppressions = mutableListOf<HushSuppression>()
    private var unneededSuppressions = mutableListOf<HushSuppression>()
    private var invalidNotes = listOf<HushSuppression>()
    private var searchDriver = GitlabIssueSearchDriver(gitlabConfig)

    private val defaultNote = "Hush generated suppression. Please update this note before committing."

    init {
        populateNeededSuppressions()
        populateUnneededSuppressions()
        populateInvalidNotes()
    }

    /**
     * Re-initialize. Used primarily when a re-evaluation of suppressions is necessary.
     */
    fun reInitialize(suppressions: List<HushSuppression>) {
        neededSuppressions = mutableListOf()
        unneededSuppressions = mutableListOf()
        this.suppressions = suppressions
        populateNeededSuppressions()
        populateUnneededSuppressions()
    }

    /**
     * Throw an error if validation fails.
     * Will throw if non-suppressed vulnerabilities are found.
     * MAY throw if unnecessary suppressions are found (as per run parameter failOnUnneeded)
     */
    fun passOrFail(failOnUnneeded: Boolean, validateNotes: Boolean) {
        if (failOnUnneeded && neededSuppressions.isNotEmpty() && unneededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Vulnerabilities and unneeded suppressions detected. Please see report for details."))
        }

        if (neededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Vulnerabilities detected. Please see report for details."))
        }

        if (failOnUnneeded && unneededSuppressions.isNotEmpty()) {
            throw HushValidationViolation(red("Unneeded suppressions detected. Please see report for details."))
        }

        if (validateNotes && invalidNotes.isNotEmpty()) {
            throw HushValidationViolation(red("Invalid notes detected. Please see report for details."))
        }

        println(green("Project passed validation."))
    }

    fun passOrFailPipeline(failOnUnneeded: Boolean, validateNotes: Boolean) {
        try {
            passOrFail(failOnUnneeded, validateNotes)
        } catch (_: HushValidationViolation) {
            throw HushValidationViolation(red("Project failed validation."))
        }
    }

    /**
     * Print report based on validation
     * Will print non-suppressed vulnerabilities
     * MAY print unnecessary suppressions (as per run parameter outputUnneeded)
     */
    fun printReport(outputUnneeded: Boolean, outputSuggested: Boolean, validateNotes: Boolean) {
        if (neededSuppressions.isNotEmpty()) {
            val vulnVerbiage = if (neededSuppressions.size > 1) "vulnerabilities" else "vulnerability"

            println("${yellow(neededSuppressions.size.toString())} ${red("$vulnVerbiage found!")}")

            for (suppression in neededSuppressions) {
                println("   - ${suppression.cve} (${suppression.referenceUrl} ${suppression.description})")
            }

            println("\n")
        }

        if (unneededSuppressions.isNotEmpty() && outputUnneeded) {
            val multiWhitespace = Regex("\\s{2,}")
            val supVerbiage = if (unneededSuppressions.size > 1) "suppressions" else "suppression"

            println("${yellow(unneededSuppressions.size.toString())} ${red("unnecessary $supVerbiage found!")}")

            for (suppression in unneededSuppressions) {
                val notes = suppression.notes
                    ?.replace("\n", "")
                    ?.replace(multiWhitespace, " ")
                    ?.trim()

                if (notes != null) {
                    println("   - ${suppression.cve} ($notes)")
                } else {
                    println("   - ${suppression.cve}")
                }
            }

            println("\n")
        }

        if (invalidNotes.isNotEmpty() && validateNotes) {
            val supVerbiage = if (invalidNotes.size > 1) "notes" else "note"

            println("${yellow(invalidNotes.size.toString())} ${red("invalid suppression $supVerbiage found! Notes must be a valid issue URL.")}")
        }

        if (!isSuppressionFileValid() && outputSuggested) {
            println(cyan("Suggested suppressions:\n"))
            println(getSuggestedSuppressionText())
        }
    }

    /**
     * Write suggested suppressions to suppression file.
     */
    fun writeSuggestedSuppressions() {
        val suggested = getSuggestedSuppressionText()

        scanDriver.writeSuggestedSuppressions(suggested)
    }

    private fun isSuppressionFileValid(): Boolean {
        return neededSuppressions.isEmpty() && unneededSuppressions.isEmpty()
    }

    private fun populateNeededSuppressions() {
        for (vulnerability in vulnerabilities) {
            val suppression = suppressions.find { vulnerability.value.cve == it.cve }

            if (suppression?.notes == null || suppression.notes.isEmpty()) {
                var defaultNote = this.defaultNote

                if (gitlabConfig.enabled && gitlabConfig.populateNotesOnMatch) {
                    defaultNote = searchDriver.findIssueUrl(vulnerability.value.cve)
                }

                val neededSuppression = HushSuppression(vulnerability.value.cve, defaultNote)
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

    private fun populateInvalidNotes() {
        invalidNotes = scanDriver.getInvalidNotes(suppressions)
    }

    private fun getSuggestedSuppressionText(): String {
        val suggested = mutableListOf<HushSuppression>()

        if (vulnerabilities.isNotEmpty()) {
            for (vulnerability in vulnerabilities) {
                var notes = defaultNote

                if (suppressions.isNotEmpty()) {
                    val existingSuppression = suppressions.find { suppression -> suppression.cve == vulnerability.key }

                    if (existingSuppression?.notes != null) {
                        notes = existingSuppression.notes
                    }
                }

                if (neededSuppressions.isNotEmpty()) {
                    val neededSuppression = neededSuppressions.find { suppresion -> suppresion.cve == vulnerability.key }

                    if (neededSuppression?.notes != null) {
                        notes = neededSuppression.notes
                    }
                }

                val sup = HushSuppression(vulnerability.key, notes)

                suggested.add(sup)
            }
        }

        return scanDriver.getSuggestedSuppressionText(suggested)
    }
}