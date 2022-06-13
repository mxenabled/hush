package com.mx.hush.core.models

data class DependencyCheckScanReport (
    val reportSchema: String,
    val scanInfo: ScanInfo,
    val projectInfo: ProjectInfo,
    val dependencies: Array<Dependency>,
) {
    fun getVulnerabilities(): HashMap<String, Vulnerability> {
        val vulnerabilityMap: HashMap<String, Vulnerability> = HashMap()

        dependencies.forEach { dependency ->
            if (dependency.vulnerabilities != null && dependency.vulnerabilities.isNotEmpty()) {
                dependency.vulnerabilities.forEach { vulnerability ->
                    vulnerabilityMap[vulnerability.name] = vulnerability
                }
            }
        }

        return vulnerabilityMap
    }
}