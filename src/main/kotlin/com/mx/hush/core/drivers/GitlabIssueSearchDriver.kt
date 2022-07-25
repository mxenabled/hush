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

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.mx.hush.HushExtension.Companion.getHush
import com.mx.hush.core.exceptions.GitlabClientError
import com.mx.hush.core.exceptions.GitlabConfigurationViolation
import com.mx.hush.core.exceptions.GitlabServerError
import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.gitlab.GitlabIssue
import com.mx.hush.core.models.red
import org.apache.commons.validator.routines.UrlValidator
import org.gradle.api.Project

class GitlabIssueSearchDriver(project: Project) : HushIssueSearchDriver(project) {
    private var urlFallbackMessage: String = "No Gitlab issue found. Please create an issue and update this note."
    private var invalidCveMessage: String = "Not a valid CVE"
    private var invalidUrlMessage: String = "Not a valid URL"
    private var invalidUrlDeepMessage: String = "CVE not found in Gitlab issue"

    private val gitlabConfiguration = project.getHush().gitlabConfiguration

    override fun findIssueUrl(cve: String): String {
        val (_, response, result) = "${gitlabConfiguration.url}/api/v4/issues"
            .httpGet(listOf("search" to cve, "scope" to "all"))
            .authentication()
            .bearer(gitlabConfiguration.token)
            .responseObject(GitlabIssue.Deserializer())

        validateResponse(response)

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

        val (_, response, result) = "${gitlabConfiguration.url}/api/v4/issues"
            .httpGet(listOf("search" to cve, "scope" to "all", "iids[]" to issueId))
            .authentication()
            .bearer(gitlabConfiguration.token)
            .responseObject(GitlabIssue.Deserializer())

        validateResponse(response)

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

    private fun validateResponse(response: Response) {
        if (response.statusCode in 200..299) {
            return
        }

        if (response.statusCode == 401) {
            throw GitlabConfigurationViolation(red("Gitlab configuration error: API responded with 'Unauthorized' status."))
        }

        if (response.statusCode in 400..499) {
            throw GitlabClientError(red("Gitlab client error: ${response.statusCode} (${response.responseMessage})"))
        }

        if (response.statusCode in 500..599) {
            throw GitlabServerError(red("Gitlab server error: ${response.statusCode} (${response.responseMessage})"))
        }
    }
}