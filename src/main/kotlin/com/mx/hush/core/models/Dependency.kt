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
package com.mx.hush.core.models

data class Dependency (
    val isVirtual: Boolean,
    val fileName: String,
    val filePath: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
    val evidenceCollected: EvidenceCollected,
    val packages: Array<Package>,
    val vulnerabilityIds: Array<Package>? = null,
    val vulnerabilities: Array<Vulnerability>? = null,
)