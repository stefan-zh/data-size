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
         * Parses the string and determines the data size. Converts to the closest whole byte.
         *
         * 10MB   = 10_485_760 bytes
         * -2.5GB = -2_684_354_560 bytes
         * 3.54MB = 3_711_959 bytes
         * 120 KB = 122_880 bytes
         * 120 kb = 122_880 bytes
         * -.3GB  = -322_122_547 bytes
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
