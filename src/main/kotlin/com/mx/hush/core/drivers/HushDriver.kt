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