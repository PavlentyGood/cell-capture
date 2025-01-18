package ru.pavlentygood.cellcapture.party.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

class AreaTest {

    @Test
    fun `create area - sort points`() {
        val p1 = ru.pavlentygood.cellcapture.party.domain.point(3, 5)
        val p2 = ru.pavlentygood.cellcapture.party.domain.point(7, 5)
        val p3 = ru.pavlentygood.cellcapture.party.domain.point(7, 9)
        val p4 = ru.pavlentygood.cellcapture.party.domain.point(3, 9)

        ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(
            p1,
            p3
        ) shouldBe ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(p3, p1)
        ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(
            p2,
            p4
        ) shouldBe ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(p4, p2)
        ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(
            p2,
            p4
        ) shouldBe ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(p1, p3)
    }

    @Test
    fun `x distance`() {
        val x = ru.pavlentygood.cellcapture.party.domain.randomInt()
        val distance = ru.pavlentygood.cellcapture.party.domain.randomInt(from = -1000)
        val area = ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(
            first = ru.pavlentygood.cellcapture.party.domain.Point(
                x = x + distance,
                y = ru.pavlentygood.cellcapture.party.domain.randomInt()
            ),
            second = ru.pavlentygood.cellcapture.party.domain.Point(
                x = x,
                y = ru.pavlentygood.cellcapture.party.domain.randomInt()
            )
        )

        area.xDistance() shouldBe abs(distance)
    }

    @Test
    fun `y distance`() {
        val y = ru.pavlentygood.cellcapture.party.domain.randomInt()
        val distance = ru.pavlentygood.cellcapture.party.domain.randomInt(from = -1000)
        val area = ru.pavlentygood.cellcapture.party.domain.Area.Companion.from(
            first = ru.pavlentygood.cellcapture.party.domain.Point(
                x = ru.pavlentygood.cellcapture.party.domain.randomInt(),
                y = y
            ),
            second = ru.pavlentygood.cellcapture.party.domain.Point(
                x = ru.pavlentygood.cellcapture.party.domain.randomInt(),
                y = y + distance
            )
        )

        area.yDistance() shouldBe abs(distance)
    }
}
