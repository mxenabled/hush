package com.mx.hush.core.models

data class EvidenceCollected (
    val vendorEvidence: Array<Evidence>,
    val productEvidence: Array<Evidence>,
    val versionEvidence: Array<Evidence>,
)