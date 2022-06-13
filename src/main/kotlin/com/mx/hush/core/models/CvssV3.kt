package com.mx.hush.core.models

data class CvssV3 (
    val baseScore: Float,
    val attackVector: String,
    val attackComplexity: Rating,
    val privilegesRequired: Rating,
    val userInteraction: Rating,
    val scope: String,
    val confidentialityImpact: Rating,
    val integrityImpact: Rating,
    val availabilityImpact: Rating,
    val baseSecerity: Rating,
    val exploitabilityScore: String,
    val impactScore: String,
    val version: String,
)