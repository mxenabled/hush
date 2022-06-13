package com.mx.hush.core.models

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["suppress"])
@XmlRootElement(name = "suppressions", namespace = "https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd")
class DependencyCheckScanSuppressions {
    @XmlElement(name = "suppress")
    var suppress: MutableCollection<DependencyCheckScanSuppression> = mutableListOf()
}