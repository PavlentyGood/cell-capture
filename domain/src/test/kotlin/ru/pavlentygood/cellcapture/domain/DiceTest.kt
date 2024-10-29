package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.ints.shouldBeInRange
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DiceTest {

    @Test
    fun `roll dice`() {
        Dice.roll().value shouldBeInRange Dice.MIN..Dice.MAX
    }

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
