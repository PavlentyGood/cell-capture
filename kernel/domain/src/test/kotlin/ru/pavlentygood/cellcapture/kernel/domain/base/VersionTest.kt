package ru.pavlentygood.cellcapture.kernel.domain.base

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class VersionTest {

    @Test
    fun `create version`() {
        Version.from(2) shouldBeRight Version.from(2).getOrNull()
        Version.from(1) shouldBeRight Version.new().next()
        Version.from(5) shouldBeRight Version.from(6).getOrNull()!!.previous()
    }

    @ParameterizedTest
    @ValueSource(longs = [-1, 0])
    fun `illegal version from existing value`(value: Long) {
        Version.from(value) shouldBeLeft IllegalVersion
    }
}
