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

import com.mx.hush.HushExtension.Companion.registerHush
import com.mx.hush.core.HushEngine.Companion.hushScanDriver
import com.mx.hush.core.HushEngine.Companion.hushSearchDriver
import com.mx.hush.core.HushEngine.Companion.registerTaskWithAliases
import com.mx.hush.core.HushEngine.Companion.registerTaskWithSetup
import com.mx.hush.core.drivers.DependencyCheckVulnerabilityScanDriver
import com.mx.hush.core.drivers.GitlabIssueSearchDriver
import com.mx.hush.core.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project


class HushPlugin() : Plugin<Project> {
    override fun apply(project: Project) {
        // Register the Hush extension
        project.registerHush()

        // Register Hush drivers
        project.hushScanDriver = DependencyCheckVulnerabilityScanDriver(project)
        project.hushSearchDriver = GitlabIssueSearchDriver(project)

        // Register Hush tasks
        project.tasks.register("hushConfigureGitlab", ConfigureGitlabTask::class.java)
        project.registerTaskWithSetup("hushReport", ReportTask::class.java as Class<HushTask>)
        project.registerTaskWithSetup("hushValidatePipeline", ValidatePipelineTask::class.java as Class<HushTask>)
        project.registerTaskWithAliases(listOf("hushWriteSuggested", "hushWriteSuppressions"), WriteSuggestedTask::class.java as Class<HushTask>, true)
    }
}