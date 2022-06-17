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

import javax.xml.bind.annotation.*

@XmlRootElement(name = "suppress")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["notes", "cve"])
class DependencyCheckScanSuppression() {
    @XmlElement(name = "notes", required = false)
    var notes: String? = null

    @XmlElement(name = "cve", required = false)
    var cve: String? = null

    constructor(notes: String) : this() {
        this.notes = notes
    }

    constructor(notes: String, cve: String) : this() {
        this.notes = notes
        this.cve = cve
    }
}