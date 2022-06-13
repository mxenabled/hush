package com.mx.hush.core.models

data class Software (
    val id: String,
    val vulnerabilityIdMatched: String,
    val versionEndExcluding: String,
)