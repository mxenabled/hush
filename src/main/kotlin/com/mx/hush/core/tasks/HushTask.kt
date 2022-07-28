package com.mx.hush.core.tasks

import org.gradle.api.DefaultTask

abstract class HushTask : DefaultTask() {
    abstract fun setupProject()
}