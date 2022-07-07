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

import com.mx.hush.core.HushEngine
import com.mx.hush.core.drivers.DependencyCheckVulnerabilityScanDriver
import com.mx.hush.core.exceptions.GitlabConfigurationViolation
import com.mx.hush.core.exceptions.HushValidationViolation
import com.mx.hush.core.models.red
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


class HushPlugin() : Plugin<Project> {
    override fun apply(project: Project) {
        val dependencyCheckDriver = DependencyCheckVulnerabilityScanDriver(project)
        val hushEngine = HushEngine(project, dependencyCheckDriver)

        project.tasks.register("hushReport") { task ->
            task.description = "Check dependencies and output a report."
            task.doLast {
                hushEngine.analyze()
            }
        }

        project.tasks.register("hushGitlabToken") { task ->
            task.description = "Set a local Gitlab token, for use in Hush, to avoid storing in a Git repository."

            val tokenFile = File("${System.getProperty("user.home")}/hush/.gitlab-token")

            task.doFirst {
                File("${System.getProperty("user.home")}/hush").mkdirs()
                tokenFile.createNewFile()
            }

            task.doLast {
                if (hushEngine.getGitlabUrl().isBlank()) {
                    println(red("Please configure a Gitlab URL."))
                    throw GitlabConfigurationViolation("No Gitlab URL configured.")
                }

                println("Please visit ${hushEngine.getGitlabTokenUrl()}, add a token, and paste it below:")
                val token = readLine()

                if (token.isNullOrBlank()) {
                    throw HushValidationViolation("Invalid token entered. Exiting.")
                }

                tokenFile.writeText(token)
            }
        }


    }
}