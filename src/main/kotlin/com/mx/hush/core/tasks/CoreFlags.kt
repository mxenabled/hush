/**
 * Copyright 2020 MX Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.hush.core.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option

interface CoreFlags {
    @set:Option(
        option = "output-unneeded",
        description = "Report suppressions found in the suppression file which are not needed."
    )
    @get:Input
    var outputUnneeded: Boolean

    @set:Option(
        option = "no-output-unneeded",
        description = "Do not report suppressions found in the suppression file which are not needed."
    )
    @get:Input
    var noOutputUnneeded: Boolean

    @set:Option(
        option = "fail-on-unneeded",
        description = "Fail if suppressions are found in the suppression file which are not needed."
    )
    @get:Input
    var failOnUnneeded: Boolean

    @set:Option(
        option = "no-fail-on-unneeded",
        description = "Do not fail if suppressions are found in the suppression file which are not needed."
    )
    @get:Input
    var noFailOnUnneeded: Boolean

    @set:Option(
        option = "output-suggested",
        description = "Output suggested suppression file contents."
    )
    @get:Input
    var outputSuggested: Boolean

    @set:Option(
        option = "no-output-suggested",
        description = "Do not output suggested suppression file contents."
    )
    @get:Input
    var noOutputSuggested: Boolean

    @set:Option(
        option = "write-suggested",
        description = "Write suggested suppression file contents to the suppression file."
    )
    @get:Input
    var writeSuggested: Boolean

    @set:Option(
        option = "no-write-suggested",
        description = "Do not write suggested suppression file contents to the suppression file."
    )
    @get:Input
    var noWriteSuggested: Boolean

    @set:Option(
        option = "validate-notes",
        description = "Validate notes with rudimentary URL validation, ensuring it is at least a URL."
    )
    @get:Input
    var validateNotes: Boolean

    @set:Option(
        option = "no-validate-notes",
        description = "Do not validate notes with rudimentary URL validation."
    )
    @get:Input
    var noValidateNotes: Boolean
}