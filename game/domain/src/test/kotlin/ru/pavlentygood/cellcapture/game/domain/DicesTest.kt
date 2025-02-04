package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class DicesTest {

    @Test
    fun `is matched area`() {
        val dices = dices()

        dices.checkMatched(dices.first, dices.second)
        dices.checkMatched(dices.second, dices.first)
    }

    private fun Dices.checkMatched(first: Dice, second: Dice) {
        val area = mockk<Area>()
        every { area.xDistance() } returns first.value - 1
        every { area.yDistance() } returns second.value - 1

        isMatched(area) shouldBeRight true
    }

    @Test
    fun `is not matched area`() {
        val someDistanceForNotMatchedArea = 6
        val dices = dices()

        val area = mockk<Area>()
        every { area.xDistance() } returns someDistanceForNotMatchedArea
        every { area.yDistance() } returns someDistanceForNotMatchedArea

        dices.isMatched(area) shouldBeRight false
    }
}
