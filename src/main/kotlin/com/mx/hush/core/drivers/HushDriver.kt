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

/**
 * Driver class for creating implementations with plugins
 */
abstract class HushDriver(private val project: Project) {
    /**
     * All logic which should run within Hush's apply() method
     */
    abstract fun setupProject()

    /**
     * Get a list of vulnerabilities for the project
     */
    abstract fun getVulnerabilities(): HashMap<String, HushVulnerability>

    /**
     * Get a list of suppressions for the project
     */
    abstract fun getSuppressions(): List<HushSuppression>?

    /**
     * Get suggested suppression file text
     */
    abstract fun getSuggestedSuppressionText(suppressions: List<HushSuppression>): String

    /**
     * Write suggested suppression file text to the suppression file
     */
    abstract fun writeSuggestedSuppressions(suppressions: String)
}