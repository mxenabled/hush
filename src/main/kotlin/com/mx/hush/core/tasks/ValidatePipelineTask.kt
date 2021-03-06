package com.mx.hush.core.tasks

import com.mx.hush.HushExtension.Companion.getHush
import com.mx.hush.core.HushEngine
import com.mx.hush.core.HushEngine.Companion.hushScanDriver
import org.gradle.api.tasks.TaskAction

open class ValidatePipelineTask : HushTask(), GitlabFlags {
    private val hushEngine = HushEngine(project)
    private val extension = project.getHush()

    override var gitlabEnabled: Boolean = extension.gitlabConfiguration.enabled
    override var gitlabDisabled: Boolean = false
    override var gitlabUrl: String = extension.gitlabConfiguration.url
    override var gitlabToken: String = extension.gitlabConfiguration.token
    override var gitlabPopulateNotes: Boolean = extension.gitlabConfiguration.populateNotesOnMatch
    override var noGitlabPopulateNotes: Boolean = false
    override var gitlabValidateNotes: Boolean = extension.gitlabConfiguration.validateNotes
    override var noGitlabValidateNotes: Boolean = false
    override var gitlabDuplicateStrategy: String = extension.gitlabConfiguration.duplicateStrategy

    init {
        description = "Check dependencies for vulnerabilities, validate suppression file, and pass/fail without report."
        group = "Verification"
    }

    @TaskAction
    fun report() {
        handleParameters()
        hushEngine.report(true)
        hushEngine.validatePipeline()
    }

    override fun setupProject() {
        hushEngine.setupProject()

        project.afterEvaluate {
            project.tasks.named("hushValidatePipeline")
                .get()
                .dependsOn(project.tasks.named(project.hushScanDriver!!.getPrerequisiteTaskName()))
        }
    }

    private fun handleParameters() {
        extension.gitlabConfiguration.enabled = (gitlabEnabled && !gitlabDisabled)
        extension.gitlabConfiguration.url = gitlabUrl
        extension.gitlabConfiguration.token = gitlabToken
        extension.gitlabConfiguration.populateNotesOnMatch = (gitlabPopulateNotes && !noGitlabPopulateNotes)
        extension.gitlabConfiguration.validateNotes = (gitlabValidateNotes && !noGitlabValidateNotes)
        extension.gitlabConfiguration.duplicateStrategy = gitlabDuplicateStrategy
    }
}