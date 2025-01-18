package ru.pavlentygood.cellcapture.party.domain

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class DicePairTest {

    @Test
    fun `is matched area`() {
        val dicePair = dicePair()

        dicePair.checkMatched(dicePair.first, dicePair.second)
        dicePair.checkMatched(dicePair.second, dicePair.first)
    }

    private fun DicePair.checkMatched(first: Dice, second: Dice) {
        val area = mockk<Area>()
        every { area.xDistance() } returns first.value - 1
        every { area.yDistance() } returns second.value - 1

        isMatched(area) shouldBe true
    }

    @Test
    fun `is not matched area`() {
        val someDistanceForNotMatchedArea = 6
        val dicePair = dicePair()

        val area = mockk<Area>()
        every { area.xDistance() } returns someDistanceForNotMatchedArea
        every { area.yDistance() } returns someDistanceForNotMatchedArea

        dicePair.isMatched(area) shouldBe false
    }
}
