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
import com.mx.hush.core.drivers.HushIssueSearchDriver
import com.mx.hush.core.drivers.HushVulnerabilityScanDriver
import com.mx.hush.core.exceptions.HushRuntimeError
import com.mx.hush.core.models.green
import com.mx.hush.core.tasks.HushTask
import org.gradle.api.Project

class HushEngine(private var project: Project) {
    private var extension = project.getHush()

    init {
        if (scanDriver == null) {
            throw HushRuntimeError("Scan driver not registered.")
        }

        if (searchDriver == null) {
            throw HushRuntimeError("Search driver not registered.")
        }
    }

    fun validateAndReport() {
        extension.gitlabConfiguration.validateConfiguration()
        val analyzer = HushDeltaAnalyzer(project)

        if (extension.writeSuggested) {
            analyzer.writeSuggestedSuppressions()
            analyzer.reInitialize()

            println(green("Suppression file written."))
        }

        analyzer.printReport(extension.outputUnneeded, extension.outputSuggested, extension.validateNotes)
        analyzer.passOrFail(extension.failOnUnneeded, extension.validateNotes)
    }

    fun validate(forceAll: Boolean) {
        val analyzer = HushDeltaAnalyzer(project)

        if (forceAll) {
            analyzer.passOrFail(true, true)
            return
        }

        analyzer.passOrFail(extension.failOnUnneeded, extension.validateNotes)
    }

    fun validatePipeline() {
        val analyzer = HushDeltaAnalyzer(project)

        analyzer.passOrFailPipeline(true, true)
    }

    fun report(forceAll: Boolean) {
        val analyzer = HushDeltaAnalyzer(project)

        if (forceAll) {
            analyzer.printReport(true, false, true)
            return
        }

        analyzer.printReport(extension.outputUnneeded, extension.outputSuggested, extension.validateNotes)
    }

    fun writeSuggestedSuppressions() {
        val analyzer = HushDeltaAnalyzer(project)

        analyzer.writeSuggestedSuppressions()
    }

    fun canWriteSuggestedSuppressions(): Boolean {
        return scanDriver!!.canWriteSuggestedSuppressions()
    }

    fun getReportFilePath(): String {
        return scanDriver!!.getReportFilePath()
    }

    fun getSuppressionFilePath(): String {
        return scanDriver!!.getSuppressionFilePath()
    }

    fun setupProject() {
        scanDriver!!.setupProject()
    }

    companion object {
        var scanDriver: HushVulnerabilityScanDriver? = null
        var searchDriver: HushIssueSearchDriver? = null

        var Project.hushScanDriver: HushVulnerabilityScanDriver?
            get() = scanDriver
            set(value) = value?.let { registerScanDriver(it) }!!

        var Project.hushSearchDriver: HushIssueSearchDriver?
            get() = searchDriver
            set(value) = value?.let { registerSearchDriver(it) }!!

        fun Project.registerTaskWithSetup(taskName: String, clazz: Class<HushTask>) {
            val newTask = project.tasks.register(taskName, clazz).get() as HushTask

            newTask.setupProject()
        }

        fun Project.registerTaskWithAliases(taskNames: List<String>, clazz: Class<HushTask>) {
            taskNames.forEach { name ->
                project.tasks.register(name, clazz)
            }
        }

        fun Project.registerTaskWithAliases(taskNames: List<String>, clazz: Class<HushTask>, withSetup: Boolean) {
            if (withSetup) {
                taskNames.forEach { name ->
                    project.registerTaskWithSetup(name, clazz)
                }
            } else {
                project.registerTaskWithAliases(taskNames, clazz)
            }
        }

        fun registerScanDriver(driver: HushVulnerabilityScanDriver) {
            scanDriver = driver
        }

        fun registerSearchDriver(driver: HushIssueSearchDriver) {
            searchDriver = driver
        }
    }
}