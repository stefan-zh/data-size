package com.stefanzh.datasize

enum class UnitSize(
    val abbr: String,
    val bytes: Long
) {
    BYTES("B", 1),
    KILOBYTES("KB", DataSize.BYTES_PER_KILOBYTE),
    MEGABYTES("MB", DataSize.BYTES_PER_MEGABYTE),
    GIGABYTES("GB", DataSize.BYTES_PER_GIGABYTE),
    TERABYTES("TB", DataSize.BYTES_PER_TERABYTE);

    companion object {
        // store an internal map for the enums in memory
        private val enums = values().associateBy { it.abbr }

        // Retrieves the enum from the abbreviation, null if no matching enum was found
        fun fromAbbreviation(abbr: String): UnitSize? {
            return enums[abbr.toUpperCase()]
        }
    }
}
