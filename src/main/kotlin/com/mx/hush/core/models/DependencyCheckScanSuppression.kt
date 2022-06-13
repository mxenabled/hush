package com.mx.hush.core.models

import javax.xml.bind.annotation.*

@XmlRootElement(name = "suppress")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["notes", "packageUrl", "cpe", "cve"])
class DependencyCheckScanSuppression {
    @XmlElement(name = "notes", required = false)
    var notes: String = ""

    @XmlElement(name = "packageUrl", required = false)
    var packageUrl: String? = null

    @XmlElement(name = "cpe", required = false)
    var cpe: String? = null

    @XmlElement(name = "cve", required = false)
    var cve: String? = null
}