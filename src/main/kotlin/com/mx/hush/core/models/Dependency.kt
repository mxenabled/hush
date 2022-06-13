package com.mx.hush.core.models

data class Dependency (
    val isVirtual: Boolean,
    val fileName: String,
    val filePath: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
    val evidenceCollected: EvidenceCollected,
    val packages: Array<Package>,
    val vulnerabilityIds: Array<Package>? = null,
    val vulnerabilities: Array<Vulnerability>? = null,
)