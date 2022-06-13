package com.mx.hush

import com.mx.hush.core.HushEngine
import com.mx.hush.core.drivers.DependencyCheckDriver
import com.mx.hush.core.exceptions.HushValidationViolation
import com.mx.hush.core.models.red
import org.gradle.api.Plugin
import org.gradle.api.Project

class HushPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val dependencyCheckDriver = DependencyCheckDriver(project)
        val hushEngine = HushEngine(project, dependencyCheckDriver)

        project.tasks.register("hushReport") { task ->
            task.doLast {
                hushEngine.analyze()
            }
        }
    }
}