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
import com.mx.hush.core.exceptions.HushIOReadWriteViolation
import com.mx.hush.core.models.red
import org.gradle.api.tasks.TaskAction
import java.io.File

open class WriteSuggestedTask : HushTask(), GitlabFlags {
    private val hushEngine = HushEngine(project)
    private val extension = project.getHush()

    init {
        description = "Write the suggested contents of the vulnerability suppression file."
        group = "Reporting"
    }

    override var gitlabEnabled: Boolean = extension.gitlabConfiguration.enabled
    override var gitlabDisabled: Boolean = false
    override var gitlabUrl: String = extension.gitlabConfiguration.url
    override var gitlabToken: String = extension.gitlabConfiguration.token
    override var gitlabPopulateNotes: Boolean = extension.gitlabConfiguration.populateNotesOnMatch
    override var noGitlabPopulateNotes: Boolean = false
    override var gitlabDuplicateStrategy: String = extension.gitlabConfiguration.duplicateStrategy
    override var gitlabValidateNotes: Boolean = extension.gitlabConfiguration.validateNotes
    override var noGitlabValidateNotes: Boolean = false

    @TaskAction
    fun writeSuggested() {
        if (!hushEngine.canWriteSuggestedSuppressions()) {
            if(!File(hushEngine.getReportFilePath()).canRead()) {
                println(red("Could not read file at '${hushEngine.getReportFilePath()}'."))
            }

            if(!File(hushEngine.getSuppressionFilePath()).canWrite()) {
                println(red("Could not write file at '${hushEngine.getSuppressionFilePath()}'. Please make sure the file permissions are set properly."))
            }

            throw HushIOReadWriteViolation("Could not write suppressions due to a read/write violation.")
        }

        handleParameters()
        hushEngine.writeSuggestedSuppressions()
    }

    override fun setupProject() {
        if(!File(hushEngine.getReportFilePath()).canRead()) {
            hushEngine.setupProject()

            project.afterEvaluate {
                project.tasks.named("hushWriteSuppressions")
                    .get()
                    .dependsOn(project.tasks.named(project.hushScanDriver!!.getPrerequisiteTaskName()))
            }
        }
    }

    private fun handleParameters() {
        extension.gitlabConfiguration.enabled = (gitlabEnabled && !gitlabDisabled)
        extension.gitlabConfiguration.url = gitlabUrl
        extension.gitlabConfiguration.token = gitlabToken
        extension.gitlabConfiguration.populateNotesOnMatch = (gitlabPopulateNotes && !noGitlabPopulateNotes)
        extension.gitlabConfiguration.duplicateStrategy = gitlabDuplicateStrategy
    }
}