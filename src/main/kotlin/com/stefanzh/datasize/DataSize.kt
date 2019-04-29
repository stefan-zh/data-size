package com.stefanzh.datasize

data class DataSize(val size: Long) {

    constructor(bytes: Long, unit: UnitSize) : this (
        size = bytes.times(unit.bytes)
    )

    companion object {
        private val pattern = Regex("(^-?\\s?\\d*.?\\d+)\\s?(B|KB|MB|GB|TB)\$", RegexOption.IGNORE_CASE)

        const val BYTES_PER_KILOBYTE = 1024L
        const val BYTES_PER_MEGABYTE = BYTES_PER_KILOBYTE * 1024L
        const val BYTES_PER_GIGABYTE = BYTES_PER_MEGABYTE * 1024L
        const val BYTES_PER_TERABYTE = BYTES_PER_GIGABYTE * 1024L

        /**
         * Parses the string and determines the data size
         */
        fun parse(str: String): DataSize {
            val result = pattern.matchEntire(str.trim()) ?: throw IllegalArgumentException("$str is not a valid data size")
            val numeric = result.groups[1]?.value?.toDouble()!!
            val unit = result.groups[2]?.value?.let { UnitSize.fromAbbreviation(it) }!!
            val bytes = Math.round(numeric.times(unit.bytes))
            return DataSize(bytes, UnitSize.BYTES)
        }
    }
}
