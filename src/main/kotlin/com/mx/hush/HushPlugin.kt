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

import com.mx.hush.HushExtension.Companion.hush
import com.mx.hush.core.tasks.ConfigureGitlabTask
import com.mx.hush.core.tasks.ReportTask
import com.mx.hush.core.tasks.WriteSuggestedTask
import org.gradle.api.Plugin
import org.gradle.api.Project


class HushPlugin() : Plugin<Project> {
    override fun apply(project: Project) {
        project.hush()

        project.tasks.register("hushReport", ReportTask::class.java)
        project.tasks.register("hushWriteSuppressions", WriteSuggestedTask::class.java)
        project.tasks.register("hushConfigureGitlab", ConfigureGitlabTask::class.java)

        project.tasks.named("hushReport", ReportTask::class.java).orNull?.setupProject()
    }
}