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
import com.mx.hush.core.drivers.DependencyCheckVulnerabilityScanDriver
import com.mx.hush.core.exceptions.HushIOReadWriteViolation
import com.mx.hush.core.models.red
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.options.OptionValues
import java.io.File

open class WriteSuggestedTask : DefaultTask() {
    private val dependencyCheckDriver = DependencyCheckVulnerabilityScanDriver(project)
    private val hushEngine = HushEngine(project, dependencyCheckDriver)
    private val extension = project.getHush()

    init {
        description = "Write the suggested contents of the vulnerability suppression file. Requires previously running hushReport."
        group = "Reporting"
    }

    @set:Option(
        option = "gitlab-enabled",
        description = "Enable the Gitlab feature."
    )
    @get:Input
    var gitlabEnabled: Boolean = extension.gitlabConfiguration.enabled

    @set:Option(
        option = "gitlab-disabled",
        description = "Disable the Gitlab feature."
    )
    @get:Input
    var gitlabDisabled: Boolean = false

    @set:Option(
        option = "gitlab-url",
        description = "The base URL (https://mygitlab.mycompany.com) of your Gitlab instance."
    )
    @get:Input
    var gitlabUrl: String = extension.gitlabConfiguration.url

    @set:Option(
        option = "gitlab-token",
        description = "The token for making API requests to your Gitlab instance."
    )
    @get:Input
    var gitlabToken: String = extension.gitlabConfiguration.token

    @set:Option(
        option = "gitlab-populate-notes",
        description = "Populate notes in suggested suppressions with an issue URL from your Gitlab (when found)."
    )
    @get:Input
    var gitlabPopulateNotes: Boolean = extension.gitlabConfiguration.populateNotesOnMatch

    @set:Option(
        option = "no-gitlab-populate-notes",
        description = "Populate notes in suggested suppressions with an issue URL from your Gitlab (when found)."
    )
    @get:Input
    var noGitlabPopulateNotes: Boolean = false

    @set:Option(
        option = "gitlab-duplicate-strategy",
        description = "Which strategy to use when more than one issue is found in Gitlab matching the CVE (oldest/newest)"
    )
    @get:Input
    var gitlabDuplicateStrategy: String = extension.gitlabConfiguration.duplicateStrategy
    @OptionValues("gitlab-duplicate-strategy")
    fun getDuplicateStrategies(): List<String> {
        return listOf("oldest", "newest")
    }

    @TaskAction
    fun writeSuggested() {
        if (!hushEngine.canWriteSuggestedSuppressions()) {
            if(!File(hushEngine.getReportFilePath()).canRead()) {
                println(red("Could not read file at '${hushEngine.getReportFilePath()}'. Please make sure you run hushReport before attempting this task."))
            }

            if(!File(hushEngine.getSuppressionFilePath()).canWrite()) {
                println(red("Could not write file at '${hushEngine.getSuppressionFilePath()}'. Please make sure the file permissions are set properly."))
            }

            throw HushIOReadWriteViolation("Could not write suppressions due to a read/write violation.")
        }

        handleParameters()
        hushEngine.writeSuggestedSuppressions()
    }

    private fun handleParameters() {
        extension.gitlabConfiguration.enabled = (gitlabEnabled && !gitlabDisabled)
        extension.gitlabConfiguration.url = gitlabUrl
        extension.gitlabConfiguration.token = gitlabToken
        extension.gitlabConfiguration.populateNotesOnMatch = (gitlabPopulateNotes && !noGitlabPopulateNotes)
        extension.gitlabConfiguration.duplicateStrategy = gitlabDuplicateStrategy
    }
}