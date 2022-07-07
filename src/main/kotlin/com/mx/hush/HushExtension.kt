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

import com.mx.hush.core.exceptions.GitlabConfigurationViolation
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import java.io.File
import javax.inject.Inject

open class HushExtension @Inject constructor(
    objects: ObjectFactory,
) {
    var outputUnneeded: Boolean = true
    var failOnUnneeded: Boolean = true
    var outputSuggested: Boolean = true
    var writeSuggested: Boolean = false
    val gitlabConfiguration: GitlabConfiguration = objects.newInstance(GitlabConfiguration::class.java)

    fun gitlabConfiguration(action: Action<GitlabConfiguration>) {
        action.execute(gitlabConfiguration)
    }

    companion object {
        fun Project.hush(): HushExtension {
            return extensions.create("hush", HushExtension::class.java)
        }
    }
}

open class GitlabConfiguration {
    var enabled: Boolean = false
    var url: String = ""
    var token: String = ""
    var populateNotesOnMatch: Boolean = true

    fun validateConfiguration() {
        if (!enabled) {
            return
        }

        if (url.isEmpty()) {
            throw GitlabConfigurationViolation("No Gitlab URL defined.")
        }

        if (token.isEmpty()) {
            if (!localTokenExists()) {
                throw GitlabConfigurationViolation("No Gitlab token defined. Please add a token to your configuration, run the task with the gitlabConfiguration.token parameter, or run ./gradlew hushGitlabToken to set your token.")
            }

            token = File("${System.getProperty("user.home")}/hush/.gitlab-token").readText()
        }
    }

    private fun localTokenExists(): Boolean {
        return File("${System.getProperty("user.home")}/hush/.gitlab-token").exists()
    }
}
