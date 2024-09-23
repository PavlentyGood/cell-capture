package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FieldTest {

    @Test
    fun `capture cells`() {
        val playerId = playerId()
        val area = area()
        val field = field()

        field.capture(playerId, area) shouldBeRight Unit

        field.getCells()[area.from.y][area.from.x] shouldBe playerId
        field.getCells()[area.from.y][area.to.x] shouldBe playerId
        field.getCells()[area.to.y][area.from.x] shouldBe playerId
        field.getCells()[area.to.y][area.to.x] shouldBe playerId

        field.getCells().capturedCellCount() shouldBe (area.xDistance() + 1) * (area.yDistance() + 1)
    }

    @Test
    fun `capture - inaccessible area`() {
        val area = area()

        checkCaptureWithInaccessibleArea(area, area.from.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.from.x, area.to.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.to.y)
    }

    private fun checkCaptureWithInaccessibleArea(area: Area, x: Int, y: Int) {
        val otherPlayerId = playerId()

        val cells = cells()
        cells[y][x] = otherPlayerId

        val field = Field(cells = cells)

        field.capture(playerId(), area) shouldBeLeft Party.InaccessibleArea

        field.getCells().capturedCellCount() shouldBe 1
    }
}
