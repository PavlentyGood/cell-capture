package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DicePairTest {

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `is matched area`(i: Boolean) {
        val dicePair = dicePair()

        val (first, second) = when (i) {
            true -> Pair(dicePair.first.value, dicePair.second.value)
            false -> Pair(dicePair.second.value, dicePair.first.value)
        }

        val area = mockk<Area>()
        every { area.xDistance() } returns first
        every { area.yDistance() } returns second

        dicePair.isMatched(area) shouldBe  true
    }

    @Test
    fun `is not matched area`() {
        val dicePair = dicePair()

        val area = mockk<Area>()
        every { area.xDistance() } returns dicePair.first.value + 1
        every { area.yDistance() } returns dicePair.second.value + 1

        dicePair.isMatched(area) shouldBe false
    }
}
