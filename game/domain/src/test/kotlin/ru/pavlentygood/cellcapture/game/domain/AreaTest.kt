package ru.pavlentygood.cellcapture.game.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.randomInt
import kotlin.math.abs

class AreaTest {

    @Test
    fun `create area - sort points`() {
        val p1 = point(3, 5)
        val p2 = point(7, 5)
        val p3 = point(7, 9)
        val p4 = point(3, 9)

        Area.from(
            p1,
            p3
        ) shouldBe Area.from(p3, p1)
        Area.from(
            p2,
            p4
        ) shouldBe Area.from(p4, p2)
        Area.from(
            p2,
            p4
        ) shouldBe Area.from(p1, p3)
    }

    @Test
    fun `x distance`() {
        val x = randomInt()
        val distance = randomInt(from = -1000)
        val area = Area.from(
            first = Point(
                x = x + distance,
                y = randomInt()
            ),
            second = Point(
                x = x,
                y = randomInt()
            )
        )

        area.xDistance() shouldBe abs(distance)
    }

    @Test
    fun `y distance`() {
        val y = randomInt()
        val distance = randomInt(from = -1000)
        val area = Area.from(
            first = Point(
                x = randomInt(),
                y = y
            ),
            second = Point(
                x = randomInt(),
                y = y + distance
            )
        )

        area.yDistance() shouldBe abs(distance)
    }
}
