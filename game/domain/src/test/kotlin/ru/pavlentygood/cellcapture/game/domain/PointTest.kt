package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PointTest {

    @Test
    fun `create point`() {
        val x = randomInt(until = Field.WIDTH)
        val y = randomInt(until = Field.HEIGHT)
        Point.from(x, y) shouldBeRight Point(x, y)
    }

    @ParameterizedTest
    @ValueSource(ints = [Point.MIN - 1, Field.WIDTH])
    fun `create point - invalid by x`(x: Int) {
        Point.from(x, Point.MIN) shouldBeLeft Point.InvalidValue
    }

    @ParameterizedTest
    @ValueSource(ints = [Point.MIN - 1, Field.HEIGHT])
    fun `create point - invalid by y`(y: Int) {
        Point.from(Point.MIN, y) shouldBeLeft Point.InvalidValue
    }
}
