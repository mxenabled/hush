package com.mx.hush.core.models

data class ScanInfo (
    val engineVersion: String,
    val dataSource: Array<DataSource>,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScanInfo

        if (engineVersion != other.engineVersion) return false
        if (!dataSource.contentEquals(other.dataSource)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = engineVersion.hashCode()
        result = 31 * result + dataSource.contentHashCode()
        return result
    }
}