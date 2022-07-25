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
package com.mx.hush.core.tasks

import com.mx.hush.HushExtension.Companion.getHush
import com.mx.hush.core.HushEngine
import com.mx.hush.core.HushEngine.Companion.hushScanDriver
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class ReportTask : DefaultTask(), GitlabFlags, CoreFlags {
    private val hushEngine = HushEngine(project)
    private val extension = project.getHush()

    init {
        description = "Check dependencies for vulnerabilities, validate suppression file, and output a report."
        group = "Reporting"
    }

    override var outputUnneeded: Boolean = extension.outputUnneeded
    override var noOutputUnneeded: Boolean = false
    override var failOnUnneeded: Boolean = extension.failOnUnneeded
    override var noFailOnUnneeded: Boolean = false
    override var outputSuggested: Boolean = extension.outputSuggested
    override var noOutputSuggested: Boolean = false
    override var writeSuggested: Boolean = extension.writeSuggested
    override var noWriteSuggested: Boolean = false
    override var validateNotes: Boolean = extension.validateNotes
    override var noValidateNotes: Boolean = false

    override var gitlabEnabled: Boolean = extension.gitlabConfiguration.enabled
    override var gitlabDisabled: Boolean = false
    override var gitlabUrl: String = extension.gitlabConfiguration.url
    override var gitlabToken: String = extension.gitlabConfiguration.token
    override var gitlabPopulateNotes: Boolean = extension.gitlabConfiguration.populateNotesOnMatch
    override var noGitlabPopulateNotes: Boolean = false
    override var gitlabValidateNotes: Boolean = extension.gitlabConfiguration.validateNotes
    override var noGitlabValidateNotes: Boolean = false
    override var gitlabDuplicateStrategy: String = extension.gitlabConfiguration.duplicateStrategy

    @TaskAction
    fun report() {
        handleParameters()
        hushEngine.validateAndReport()
    }

    fun setupProject() {
        hushEngine.setupProject()

        project.afterEvaluate {
            project.tasks.named("hushReport")
                .get()
                .dependsOn(project.tasks.named(project.hushScanDriver!!.getPrerequisiteTaskName()))
        }
    }

    private fun handleParameters() {
        extension.outputUnneeded = (outputUnneeded && !noOutputUnneeded)
        extension.failOnUnneeded = (failOnUnneeded && !noFailOnUnneeded)
        extension.outputSuggested = (outputSuggested && !noOutputSuggested)
        extension.writeSuggested = (writeSuggested && !noWriteSuggested)
        extension.validateNotes = (validateNotes && !noValidateNotes)
        extension.gitlabConfiguration.enabled = (gitlabEnabled && !gitlabDisabled)
        extension.gitlabConfiguration.url = gitlabUrl
        extension.gitlabConfiguration.token = gitlabToken
        extension.gitlabConfiguration.populateNotesOnMatch = (gitlabPopulateNotes && !noGitlabPopulateNotes)
        extension.gitlabConfiguration.validateNotes = (gitlabValidateNotes && !noGitlabValidateNotes)
        extension.gitlabConfiguration.duplicateStrategy = gitlabDuplicateStrategy
    }
}