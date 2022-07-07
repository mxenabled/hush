# Hush

A wrapper for dependency vulnerability plugins which makes it easier to manage suppression files.

Jitpack: https://jitpack.io/p/mxenabled/hush

## Use

### Basic Usage

In a terminal, simply run `./gradlew hushReport` to get a report of new vulnerabilities, unnecessary suppressions, and 
suggested suppression file contents.

### Arguments / Config

The following flags can be leveraged to alter behavior:

   - `outputUnneeded` (**enabled** by default): Output the list of suppressions which are not necessary, in the Hush 
Report.
   - `failOnUnneeded` (**enabled** by default): Throw if there are unnecessary suppressions in the suppression file.
   - `outputSuggested` (**enabled** by default): Output the suggested suppression file contents.
   - `writeSuggested` (**disabled** by default): Write the suggested suppressions directly to the suppression file.
   - `gitlab` (**disabled** by default -- parameter only): Enable the Gitlab issue searching feature.
   - `gitlabConfiguration`:
     - `enabled`: Whether the Gitlab configuration is enabled.
     - `url`: The base URL of your Gitlab instance.
     - `token`: A valid token for interacting with your Gitlab API.
     - `populateNotesOnMatch`: Add a matching Gitlab issue URL to the notes of suppressions.
     - `duplicateStrategy`: Either `oldest` or `newest`. If more than one issue is found matching a CVE, which one to use.

You may prepend `no` to any of the above flags, and the feature will be disabled (such as `noOutputUnneeded`).

The above flags may also be used in `build.gradle` as config options. See below for examples.

### Examples

To disable the reporting of unnecessary suppressions and suggestions, and to write directly to the suppression file
(perhaps in a dev environment), you could run the following:

#### In terminal

```
./gradlew hushReport -PnoOutputUnneeded -PnoOutputSuggested -PwriteSuggested
```

To disable the reporting of suggestions, you could run the following:

#### In terminal

```
./gradlew hushReport -PnoOutputSuggested
```

Example configuration with default values:

#### In `build.gradle`

```
hush {
   outputUnneeded = true
   failOnUnneeded = true
   outputSuggested = true
   writeSuggested = false
   gitlabConfiguration {
    enabled = false
    url = ""
    token = ""
    populateNotesOnMatch = true
    duplicateStrategy = "oldest"
  }
}
```

### Manually Running Sub-Tasks

Subtasks use their original names. You may run them per usual. For example:

   - `./gradlew dependencyCheck`
   - `./gradlew dependencyCheckAnalyze`

### Local development workflow with Gitlab tokens

`./gradlew hushGitlabToken` will enable you to store a token locally, so you do not need to add a token to your `build.gradle`.

First, configure your Gitlab URL. Then, run the task (`./gradlew hushGitlabToken`). You will be given a URL to go to (but you can always just navigate to your profile in Gitlab) and generate a token. It is highly recommended that the token you generate has **read-only access**, for security purposes. Hush does not currently have any need for write permissions at all.

When you run this task and input your token, a file will be created in `<user home>/hush` called `.gitlab-token`. This file will act as a fallback for scenarios that the token is not available via configuration or parameters. This allows an organization to avoid committing tokens to source control.

#### Notes regarding overrides

If you use the local development token strategy, and later configure a token (use supply a token via command line), it will be overridden. The token file is a fallback which is only used if a token is not configured. Please keep this in mind.