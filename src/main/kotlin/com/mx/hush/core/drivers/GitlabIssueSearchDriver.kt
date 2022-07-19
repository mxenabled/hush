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
import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.gitlab.GitlabIssue
import org.apache.commons.validator.routines.UrlValidator

class GitlabIssueSearchDriver(private val gitlabConfiguration: GitlabConfiguration) : HushIssueSearchDriver() {
    private var urlFallbackMessage: String = "No Gitlab issue found. Please create an issue and update this note."
    private var invalidCveMessage: String = "Not a valid CVE"
    private var invalidUrlMessage: String = "Not a valid URL"
    private var invalidUrlDeepMessage: String = "CVE not found in Gitlab issue"

    override fun findIssueUrl(cve: String): String {
        val (_, _, result) = "${gitlabConfiguration.url}/api/v4/issues"
            .httpGet(listOf("search" to cve, "scope" to "all"))
            .authentication()
            .bearer(gitlabConfiguration.token)
            .responseObject(GitlabIssue.Deserializer())

        val issues = result.component1() ?: return urlFallbackMessage

        if (issues.isEmpty()) {
            return urlFallbackMessage
        }

        if (gitlabConfiguration.duplicateStrategy != "oldest") {
            return issues.first().webUrl
        }

        return issues.last().webUrl
    }

    /**
     * Performs deep validation on a URL, ensuring it is a valid Gitlab issue with the CVE in the title or body
     */
    override fun isValidUrlDeep(url: String, cve: String): Boolean {
        val pieces = url.split("/")
        val issueId = pieces[pieces.size - 1]

        val (_, _, result) = "${gitlabConfiguration.url}/api/v4/issues"
            .httpGet(listOf("search" to cve, "scope" to "all", "iids[]" to issueId))
            .authentication()
            .bearer(gitlabConfiguration.token)
            .responseObject(GitlabIssue.Deserializer())

        val issues = result.component1() ?: return false

        if (issues.isEmpty()) {
            return false
        }

        return true
    }

    /**
     * Performs rudimentary validation on a URL, ensuring it is in fact a URL
     */
    override fun isValidUrlSimple(url: String): Boolean {
        val validator = UrlValidator()
        return validator.isValid(url)
    }

    /**
     * Get all invalid notes
     */
    override fun getInvalidNotes(suppressions: List<HushSuppression>): List<HushSuppression> {
        val invalidNotes = mutableListOf<HushSuppression>()

        suppressions.forEach { suppression ->
            if (suppression.notes == null) {
                invalidNotes.add(HushSuppression(suppression.cve, "No note set. Please define a valid Gitlab issue URL."))
            } else if (suppression.cve == null) {
                invalidNotes.add(HushSuppression("UNDEFINED", "${suppression.notes} - $invalidCveMessage"))
            } else if (!isValidUrlSimple(suppression.notes)) {
                invalidNotes.add(HushSuppression(suppression.cve, "${suppression.notes} - $invalidUrlMessage"))
            } else if (!isValidUrlDeep(suppression.notes, suppression.cve)) {
                invalidNotes.add(HushSuppression(suppression.cve, "${suppression.notes} - $invalidUrlDeepMessage"))
            }
        }

        return invalidNotes
    }
}