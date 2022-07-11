package com.mx.hush.core.tasks

import com.google.gson.Gson
import com.mx.hush.GitlabConfiguration
import com.mx.hush.HushExtension
import com.mx.hush.core.models.green
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

open class ConfigureGitlabTask : DefaultTask() {
    init {
        description = "Setup a local configuration for the Gitlab integration."
        group = "Reporting"
    }

    @set:Option(
        option = "url",
        description = "The base URL (https://mygitlab.mycompany.com) of your Gitlab instance."
    )
    @get:Input
    var url: String = ""

    @set:Option(
        option = "token",
        description = "The token for making API requests to your Gitlab instance."
    )
    @get:Input
    var token: String = ""

    @set:Option(
        option = "populate-notes",
        description = "Populate notes in suggested suppressions with an issue URL from your Gitlab (when found)."
    )
    @get:Input
    var populateNotes: Boolean = false

    @set:Option(
        option = "no-populate-notes",
        description = "Do not populate notes in suggested suppressions with an issue URL from your Gitlab (when found)."
    )
    @get:Input
    var noPopulateNotes: Boolean = false

    @set:Option(
        option = "duplicate-strategy",
        description = "Which strategy to use when more than one issue is found in Gitlab matching the CVE (oldest/newest)"
    )
    @get:Input
    var duplicateStrategy: String = ""

    @TaskAction
    fun configureGitlab() {
        val configFile = File(HushExtension.gitlabConfigPath + HushExtension.gitlabConfigFile)

        File(HushExtension.gitlabConfigPath).mkdirs()
        configFile.createNewFile()

        if (url.isEmpty()) {
            println("Please enter your Gitlab base URL (ex. https://mygitlab.mydomain.com)")
            url = readLine().toString()
        }

        if (token.isEmpty()) {
            println("Please visit $url/-/profile/personal_access_tokens, add a token, and paste it below:")
            token = readLine().toString()
        }

        if (!populateNotes && !noPopulateNotes) {
            println("Would you like to populate suppression notes with Gitlab issue URLs? (y/n)")
            populateNotes = readLine().toString().toLowerCase() == "y"
        }

        if (duplicateStrategy.isEmpty()) {
            println("If duplicate issues are found, should Hush pick the oldest or newest issue? (oldest/newest)")
            duplicateStrategy = if (readLine().toString().toLowerCase() == "oldest") "oldest" else "newest"
        }

        val gitlabConfig = GitlabConfiguration()
        gitlabConfig.enabled = true
        gitlabConfig.url = url
        gitlabConfig.token = token
        gitlabConfig.populateNotesOnMatch = (populateNotes && !noPopulateNotes)
        gitlabConfig.duplicateStrategy = duplicateStrategy

        configFile.writeText(Gson().toJson(gitlabConfig, GitlabConfiguration::class.java))

        println(green("Configuration written to ${HushExtension.gitlabConfigPath}${HushExtension.gitlabConfigFile}"))
    }
}