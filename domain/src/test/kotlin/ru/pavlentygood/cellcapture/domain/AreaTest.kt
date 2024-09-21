package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

class AreaTest {

    @Test
    fun `x distance`() {
        val x = randomInt()
        val distance = randomInt(from = -1000)
        val area = Area(
            from = Point(x = x + distance, y = randomInt()),
            to = Point(x = x, y = randomInt())
        )

        area.xDistance() shouldBe abs(distance)
    }

    @Test
    fun `y distance`() {
        val y = randomInt()
        val distance = randomInt(from = -1000)
        val area = Area(
            from = Point(x = randomInt(), y = y),
            to = Point(x = randomInt(), y = y + distance)
        )

        area.yDistance() shouldBe abs(distance)
    }
}
