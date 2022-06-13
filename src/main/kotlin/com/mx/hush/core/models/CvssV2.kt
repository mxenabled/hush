package com.mx.hush.core.models

data class CvssV2 (
    val score: Float,
    val accessVector: String,
    val accessComplexity: Rating,
    val authenticationr: String,
    val confidentialImpact: String,
    val integrityImpact: String,
    val availabilityImpact: String,
    val severity: Rating,
    val version: String,
    val exploitabilityScore: String,
)