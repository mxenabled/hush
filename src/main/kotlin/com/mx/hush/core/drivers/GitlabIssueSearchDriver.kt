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
package com.mx.hush.core.drivers

import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.mx.hush.GitlabConfiguration
import com.mx.hush.core.models.gitlab.GitlabIssue

class GitlabIssueSearchDriver(private val gitlabConfiguration: GitlabConfiguration) : HushIssueSearchDriver() {
    private var urlFallbackMessage: String = "No Gitlab issue found. Please create and issue and update this note."

    override fun findIssueUrl(cve: String): String {
        val (_, _, result) = "${gitlabConfiguration.url}/api/v4/issues"
            .httpGet(listOf("search" to cve, "scope" to "all"))
            .authentication()
            .bearer(gitlabConfiguration.token)
            .responseObject(GitlabIssue.Deserializer())

        val issues = result.component1() ?: return urlFallbackMessage

        return issues.last().webUrl
    }

    override fun isValidNote(note: String): Boolean {
        TODO("Not yet implemented")
    }
}