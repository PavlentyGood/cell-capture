package io.github.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PointTest {

    @ParameterizedTest
    @ValueSource(ints = [Point.MIN - 1, Field.WIDTH])
    fun `create point - illegal by x`(x: Int) {
        Point.from(x, Point.MIN) shouldBeLeft Point.InvalidValue
    }

    @ParameterizedTest
    @ValueSource(ints = [Point.MIN - 1, Field.HEIGHT])
    fun `create point - illegal by y`(y: Int) {
        Point.from(Point.MIN, y) shouldBeLeft Point.InvalidValue
    }
}
