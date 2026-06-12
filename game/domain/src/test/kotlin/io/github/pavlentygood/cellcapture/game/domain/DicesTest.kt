package io.github.pavlentygood.cellcapture.game.domain

import io.github.pavlentygood.cellcapture.kernel.domain.randomInt
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest
    @ValueSource(ints = [Dice.MIN - 1, Dice.MAX + 1])
    fun `illegal dice value`(value: Int) {
        Dices.restore(value, 1) shouldBeLeft Dice.InvalidValue
    }
}
