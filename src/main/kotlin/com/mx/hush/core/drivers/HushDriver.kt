package com.mx.hush.core.drivers

import com.mx.hush.core.models.HushSuppression
import com.mx.hush.core.models.HushVulnerability
import org.gradle.api.Project

abstract class HushDriver(private val project: Project) {
    abstract fun setupProject()
    abstract fun getVulnerabilities(): HashMap<String, HushVulnerability>
    abstract fun getSuppressions(): List<HushSuppression>?
    abstract fun getSuggestedSuppressionText(suppressions: List<HushSuppression>): String
    abstract fun outputSuggestedSuppressions(suppressions: String)
}