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
package com.mx.hush

import com.google.gson.Gson
import com.mx.hush.core.exceptions.GitlabConfigurationViolation
import com.mx.hush.core.models.red
import org.apache.commons.lang3.StringUtils
import org.apache.commons.validator.routines.UrlValidator
import org.gradle.api.Project
import java.io.File

open class HushExtension {
    var outputUnneeded: Boolean = true
    var failOnUnneeded: Boolean = true
    var outputSuggested: Boolean = true
    var writeSuggested: Boolean = false
    var validateNotes: Boolean = true
    var gitlabConfiguration: GitlabConfiguration = GitlabConfiguration()

    init {
        if (configFile.exists()) {
            val config: GitlabConfiguration = Gson().fromJson(
                configFile.readText(),
                GitlabConfiguration::class.java)

            config.url = StringUtils.removeEnd(config.url, "/")

            if (!UrlValidator().isValid(config.url)) {
                println(red("Invalid Gitlab URL ('${config.url}') defined. Gitlab search will not be used for CVEs."))
            } else {
                gitlabConfiguration = config
            }
        } else {
            val environmentVars = System.getenv()

            if (environmentVars["HUSH_GITLAB_ENABLED"]?.isNotBlank() == true) {
                gitlabConfiguration.enabled = environmentVars["HUSH_GITLAB_ENABLED"].toBoolean()
            }

            if (environmentVars["HUSH_GITLAB_URL"]?.isNotBlank() == true) {
                gitlabConfiguration.url = environmentVars["HUSH_GITLAB_URL"].toString()
            }

            if (environmentVars["HUSH_GITLAB_TOKEN"]?.isNotBlank() == true) {
                gitlabConfiguration.token = environmentVars["HUSH_GITLAB_TOKEN"].toString()
            }

            if (environmentVars["HUSH_GITLAB_POPULATE_NOTES"]?.isNotBlank() == true) {
                gitlabConfiguration.populateNotesOnMatch = environmentVars["HUSH_GITLAB_POPULATE_NOTES"].toBoolean()
            }

            if (environmentVars["HUSH_GITLAB_DUPLICATE_STRATEGY"]?.isNotBlank() == true) {
                gitlabConfiguration.duplicateStrategy = environmentVars["HUSH_GITLAB_DUPLICATE_STRATEGY"].toString()
            }

            if (environmentVars["HUSH_GITLAB_VALIDATE_NOTES"]?.isNotBlank() == true) {
                gitlabConfiguration.validateNotes = environmentVars["HUSH_GITLAB_VALIDATE_NOTES"].toBoolean()
            }
        }
    }

    companion object {
        val configPath = "${System.getProperty("user.home")}/.config/hush/"
        const val configFileName = "hush-config.json"
        val configFile = File(configPath + configFileName)

        fun Project.registerHush(): HushExtension {
            return extensions.create("hush", HushExtension::class.java)
        }

        fun Project.getHush(): HushExtension {
            return extensions.getByType(HushExtension::class.java)
        }
    }
}

open class GitlabConfiguration {
    var enabled: Boolean = false
    var url: String = ""
    var token: String = ""
    var populateNotesOnMatch: Boolean = true
    var duplicateStrategy: String = "oldest"
    var validateNotes: Boolean = true

    fun validateConfiguration() {
        if (!enabled) {
            return
        }

        if (url.isEmpty()) {
            throw GitlabConfigurationViolation("No Gitlab URL defined.")
        }

        if (token.isEmpty()) {
            throw GitlabConfigurationViolation("No Gitlab token defined. Please configure it as an environment variable, run the task with the gitlab-token parameter, or run ./gradlew hushConfigureGitlab.")
        }
    }
}
