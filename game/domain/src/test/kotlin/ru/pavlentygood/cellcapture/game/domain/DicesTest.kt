package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.randomInt

class DicesTest {

    @Test
    fun `restore rolled dices`() {
        val dices = Dices.restore(randomInt(1, 7), randomInt(1, 7)).shouldBeRight()
        dices.firstValue?.shouldBeInRange(1..6)
        dices.secondValue?.shouldBeInRange(1..6)
        dices.rolled shouldBe true
    }

    @Test
    fun `restore not rolled dices`() {
        val dices = Dices.restore(null, null).shouldBeRight()
        dices.firstValue shouldBe null
        dices.secondValue shouldBe null
        dices.rolled shouldBe false
    }

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

        isNotMatched(area) shouldBe false
    }

    @Test
    fun `is not matched area`() {
        val someDistanceForNotMatchedArea = 6
        val dices = dices()

        val area = mockk<Area>()
        every { area.xDistance() } returns someDistanceForNotMatchedArea
        every { area.yDistance() } returns someDistanceForNotMatchedArea

        dices.isNotMatched(area) shouldBe true
    }
}
