package com.stefanzh.datasize

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class DataSizeTest {

    @Nested
    @DisplayName("parse")
    internal inner class Parse {
        @Test
        fun `missing numeric part`() {
            val ex = assertThrows(IllegalArgumentException::class.java) {
                DataSize.parse("MB")
            }
            assertEquals("MB is not a valid data size", ex.message)
        }

        @Test
        fun `missing data size unit part`() {
            val ex = assertThrows(IllegalArgumentException::class.java) {
                DataSize.parse("10")
            }
            assertEquals("10 is not a valid data size", ex.message)
        }

        @Test
        fun `invalid beginning of the string`() {
            val ex = assertThrows(IllegalArgumentException::class.java) {
                DataSize.parse("--10.0mB")
            }
            assertEquals("--10.0mB is not a valid data size", ex.message)
        }

        @Test
        fun `random text`() {
            val ex = assertThrows(IllegalArgumentException::class.java) {
                DataSize.parse("21kjsgka")
            }
            assertEquals("21kjsgka is not a valid data size", ex.message)
        }

        @ParameterizedTest
        @EnumSource(UnitSize::class)
        fun `positive values`(unit: UnitSize) {
            val expected = DataSize(10, unit)
            val actual = DataSize.parse("10${unit.abbr}")
            assertEquals(expected, actual)
        }
    }
}
