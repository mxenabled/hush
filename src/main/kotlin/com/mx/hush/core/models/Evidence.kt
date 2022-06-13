package com.mx.hush.core.models

data class Evidence (
    val type: String,
    val confidence: Rating,
    val source: String,
    val name: String,
    val value: String,
)