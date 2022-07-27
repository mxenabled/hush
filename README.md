# Hush

A wrapper for dependency vulnerability plugins which makes it easier to manage suppression files.

Jitpack: https://jitpack.io/p/mxenabled/hush

## Commands
The following commands are available:

- `./gradlew hushReport`: Scan for vulnerabilities in dependencies, unneeded suppressions in the suppression file, and 
output a report.
- `./gradlew hushWriteSuppressions`: Write the suggested suppressions to the suppression file.
- `./gradlew hushConfigureGitlab`: Step-by-step configuration of Gitlab, which will be stored in
  `<user home>/.config/hush/hush-config.json` to prevent committing tokens to source control. Alternatively, this file can be 
manually created / edited with these properties: `enabled`, `url`, `token`, `populateNotesOnMatch`, `validateNotes`, and 
`duplicateStrategy`.
- `./gradlew hushValidatePipeline`: Perform validation without consideration for configuration values. Fails when 
unneeded suppressions or invalid notes are found. Outputs a verbose report without suggestions.

## Arguments / Config

### Command Line Arguments

#### hushReport

- `--output-unneeded` or `--no-output-unneeded`: Enable/disable reporting unneeded suppressions.
- `--output-suggested` or `--no-output-suggested`: Enable/disable reporting suggested suppression file contents.
- `--write-suggested` or `--no-write-suggested`: Enable/disable writing suggested suppression file contents to the 
suppression file.
- `--validate-notes` or `--no-validate-notes`: Enable/disable rudimentary URL validation for notes within suppressions.
- `--gitlab-enabled` or `--gitlab-disabled`: Enable/disable the Gitlab issue searching feature.
- `--gitlab-url=XXXX`: Set the Gitlab base URL.
- `--gitlab-token=XXXX`: Set the Gitlab API token.
- `--gitlab-populate-notes` or `--no-gitlab-populate-notes`: Enable/disable populating notes with Gitlab issue URLs.
- `--gitlab-validate-notes` or `--no-gitlab-validate-notes`: Enable/disable validating notes as Gitlab issue URLs, with
the CVE in the issue.
- `--gitlab-duplicate-strategy=[oldest, newest]`: When more than one issue is found, which issue to use (valid options: 
`oldest` or `newest`).

#### hushConfigureGitlab

- `--url=XXXX`: Set the Gitlab base URL.
- `--token=XXXX`: Set the Gitlab API token.
- `--populate-notes` or `--no-populate-notes`: Enable/disable populating notes with Gitlab issue URLs.
- `--validate-notes` or `--no-validate-notes`: Enable/disable validating notes as Gitlab issues.
- `--duplicate-strategy=[oldest, newest]`: When more than one issue is found, which issue to use (valid options: 
`oldest` or `newest`).

#### hushWriteSuppressions

- `--gitlab-enabled` or `--gitlab-disabled`: Enable/disable the Gitlab issue searching feature.
- `--gitlab-url=XXXX`: Set the Gitlab base URL.
- `--gitlab-token=XXXX`: Set the Gitlab API token.
- `--gitlab-populate-notes` or `--no-gitlab-populate-notes`: Enable/disable populating notes with Gitlab issue URLs.
- `--gitlab-duplicate-strategy=[oldest, newest]`: When more than one issue is found, which issue to use (valid options:
  `oldest` or `newest`).
  
#### hushValidatePipeline

- `--gitlab-enabled` or `--gitlab-disabled`: Enable/disable the Gitlab issue searching feature.
- `--gitlab-url=XXXX`: Set the Gitlab base URL.
- `--gitlab-token=XXXX`: Set the Gitlab API token.
- `--gitlab-validate-notes` or `--no-gitlab-validate-notes`: Enable/disable validating notes as Gitlab issue URLs, with
the CVE in the issue.

### Config
Available configuration properties and their default values:

```
hush {
   outputUnneeded = true
   failOnUnneeded = true
   outputSuggested = true
   writeSuggested = false
   validateNotes = true // Rudimentary URL validation, NOT integration-based validation (like Gitlab)
}
```

## Local development workflow with Gitlab

`./gradlew hushConfigureGitlab` will step you through creating a local configuration for Gitlab.

You will be given a URL to go to (but you can always just navigate to your profile in Gitlab) and generate a token. It 
is highly recommended that the token you generate has **read-only access**, for security purposes. Hush does not 
currently have any need for write permissions at all.

When you run this task, a file will be created in `<user home>/.config/hush` called `hush-config.json`. This file will 
act as a fallback for scenarios that the Gitlab configuration is not supplied via parameters. This allows an 
organization to avoid committing tokens to source control.

### Notes regarding overrides

If you use the local development strategy, and supply config parameters via command line, command line values will 
always override the local configuration.

## Environment variable workflow with Gitlab

You may also define environment variables for the Gitlab configuration:

```
HUSH_GITLAB_ENABLED=false
HUSH_GITLAB_URL=""
HUSH_GITLAB_TOKEN=""
HUSH_GITLAB_POPULATE_NOTES=true
HUSH_GITLAB_VALIDATE_NOTES=true
HUSH_GITLAB_DUPLICATE_STRATEGY="oldest"
```

Please note that a local configuration will be used when the file exists, and that command line parameters will always 
override configured values. Thus, in order to utilize environment variables, a local config cannot exist, and the 
parameters must not be defined via command line.

## Caveats with writing suggested suppressions

When the `--write-suggested` flag is passed to the `hushReport` task, the report will run _after_ the suppressions have 
been written. Thus, it is important to ensure that you **do not** write suggested suppressions in a pipeline flow. If 
you do, the pipeline will always pass, and you will never see vulnerabilities.
