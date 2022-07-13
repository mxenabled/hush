package com.mx.hush.core.tasks

import com.mx.hush.core.HushEngine
import com.mx.hush.core.drivers.DependencyCheckVulnerabilityScanDriver
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class ValidatePipelineTask : DefaultTask() {
    private val dependencyCheckDriver = DependencyCheckVulnerabilityScanDriver(project)
    private val hushEngine = HushEngine(project, dependencyCheckDriver)

    init {
        description = "Check dependencies for vulnerabilities, validate suppression file, and pass/fail without report."
        group = "Verification"
    }

    @TaskAction
    fun report() {
        hushEngine.report(true)
        hushEngine.validatePipeline()
    }

    fun setupProject() {
        hushEngine.setupProject()
    }
}