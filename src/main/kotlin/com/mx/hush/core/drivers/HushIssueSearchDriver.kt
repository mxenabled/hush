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

import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.HushVulnerability
import org.gradle.api.Project

abstract class HushIssueSearchDriver(project: Project) {
    /**
     * Search via API request for a CVE
     */
    abstract fun findIssueUrl(cve: String): String

    /**
     * Get a list of Gitlab Issue URLs from a list of vulnerabilities
     */
    abstract suspend fun getIssueUrlsAsync(vulnerabilities: List<HushVulnerability>): List<HushSuppression>

    /**
     * Performs deep validation on a URL, ensuring it is a valid issue with the CVE in the title or body
     */
    abstract fun isValidUrlDeep(url: String, cve: String): Boolean

    /**
     * Performs rudimentary validation on a URL, ensuring it is in fact a URL
     */
    abstract fun isValidUrlSimple(url: String): Boolean

    /**
     * Returns a list of suppressions with invalid notes
     */
    abstract fun getInvalidNotes(suppressions: List<HushSuppression>): List<HushSuppression>
    abstract suspend fun getInvalidNotesAsync(suppressions: List<HushSuppression>): List<HushSuppression>
}