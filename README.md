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
