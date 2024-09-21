package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DiceTest {

    @Test
    fun `create dice`() {
        val value = randomInt(from = Dice.MIN, until = Dice.MAX + 1)
        Dice.from(value) shouldBeRight Dice(value)
    }

    @ParameterizedTest
    @ValueSource(ints = [Dice.MIN - 1, Dice.MAX + 1])
    fun `create dice - invalid`(value: Int) {
        Dice.from(value) shouldBeLeft Dice.InvalidValue
    }
}
