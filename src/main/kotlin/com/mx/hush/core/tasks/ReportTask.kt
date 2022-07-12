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
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.options.OptionValues

open class ReportTask : DefaultTask() {
    private val dependencyCheckDriver = DependencyCheckVulnerabilityScanDriver(project)
    private val hushEngine = HushEngine(project, dependencyCheckDriver)
    private val extension = project.getHush()

    init {
        description = "Check dependencies for vulnerabilities, validate suppression file, and output a report."
        group = "Reporting"
    }

    @set:Option(
        option = "output-unneeded",
        description = "Report suppressions found in the suppression file which are not needed."
    )
    @get:Input
    var outputUnneeded: Boolean = extension.outputUnneeded

    @set:Option(
        option = "no-output-unneeded",
        description = "Do not report suppressions found in the suppression file which are not needed."
    )
    @get:Input
    var noOutputUnneeded: Boolean = false

    @set:Option(
        option = "fail-on-unneeded",
        description = "Fail if suppressions are found in the suppression file which are not needed."
    )
    @get:Input
    var failOnUnneeded: Boolean = extension.failOnUnneeded

    @set:Option(
        option = "no-fail-on-unneeded",
        description = "Do not fail if suppressions are found in the suppression file which are not needed."
    )
    @get:Input
    var noFailOnUnneeded: Boolean = false

    @set:Option(
        option = "output-suggested",
        description = "Output suggested suppression file contents."
    )
    @get:Input
    var outputSuggested: Boolean = extension.outputSuggested

    @set:Option(
        option = "no-output-suggested",
        description = "Do not output suggested suppression file contents."
    )
    @get:Input
    var noOutputSuggested: Boolean = false

    @set:Option(
        option = "write-suggested",
        description = "Write suggested suppression file contents to the suppression file."
    )
    @get:Input
    var writeSuggested: Boolean = extension.writeSuggested

    @set:Option(
        option = "no-write-suggested",
        description = "Do not write suggested suppression file contents to the suppression file."
    )
    @get:Input
    var noWriteSuggested: Boolean = false

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
    fun report() {
        handleParameters()
        hushEngine.analyze()
    }

    fun setupProject() {
        hushEngine.setupProject()
    }

    private fun handleParameters() {
        extension.outputUnneeded = (outputUnneeded && !noOutputUnneeded)
        extension.failOnUnneeded = (failOnUnneeded && !noFailOnUnneeded)
        extension.outputSuggested = (outputSuggested && !noOutputSuggested)
        extension.writeSuggested = (writeSuggested && !noWriteSuggested)
        extension.gitlabConfiguration.enabled = (gitlabEnabled && !gitlabDisabled)
        extension.gitlabConfiguration.url = gitlabUrl
        extension.gitlabConfiguration.token = gitlabToken
        extension.gitlabConfiguration.populateNotesOnMatch = (gitlabPopulateNotes && !noGitlabPopulateNotes)
        extension.gitlabConfiguration.duplicateStrategy = gitlabDuplicateStrategy
    }
}