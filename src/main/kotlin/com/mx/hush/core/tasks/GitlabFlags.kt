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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.options.OptionValues

interface GitlabFlags {
    @set:Option(
        option = "gitlab-enabled",
        description = "Enable the Gitlab feature."
    )
    @get:Input
    var gitlabEnabled: Boolean

    @set:Option(
        option = "gitlab-disabled",
        description = "Disable the Gitlab feature."
    )
    @get:Input
    var gitlabDisabled: Boolean

    @set:Option(
        option = "gitlab-url",
        description = "The base URL (https://mygitlab.mycompany.com) of your Gitlab instance."
    )
    @get:Input
    var gitlabUrl: String

    @set:Option(
        option = "gitlab-token",
        description = "The token for making API requests to your Gitlab instance."
    )
    @get:Input
    var gitlabToken: String

    @set:Option(
        option = "gitlab-populate-notes",
        description = "Populate notes in suggested suppressions with an issue URL from your Gitlab (when found)."
    )
    @get:Input
    var gitlabPopulateNotes: Boolean

    @set:Option(
        option = "no-gitlab-populate-notes",
        description = "Do not populate notes in suggested suppressions with an issue URL from your Gitlab."
    )
    @get:Input
    var noGitlabPopulateNotes: Boolean

    @set:Option(
        option = "gitlab-validate-notes",
        description = "Validate notes in suppressions, ensuring a valid Gitlab ticket is found."
    )
    @get:Input
    var gitlabValidateNotes: Boolean

    @set:Option(
        option = "no-gitlab-validate-notes",
        description = "Do not validate notes in suppressions, ensuring a valid Gitlab ticket is found."
    )
    @get:Input
    var noGitlabValidateNotes: Boolean

    @set:Option(
        option = "gitlab-duplicate-strategy",
        description = "Which strategy to use when more than one issue is found in Gitlab matching the CVE (oldest/newest)"
    )
    @get:Input
    var gitlabDuplicateStrategy: String
    @OptionValues("gitlab-duplicate-strategy")
    fun getDuplicateStrategies(): List<String> {
        return listOf("oldest", "newest")
    }
}