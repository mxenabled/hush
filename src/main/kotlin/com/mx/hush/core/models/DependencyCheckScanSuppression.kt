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