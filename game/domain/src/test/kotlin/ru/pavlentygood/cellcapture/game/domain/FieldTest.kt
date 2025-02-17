package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class FieldTest {

    @Test
    fun `capture cells`() {
        val playerId = playerId()
        val area = area(distanceToEdges = 1)

        val cells = cells()
        cells.setCell(playerId, area.from.x - 1, area.from.y)

        val field = field(cells = cells)

        field.capture(playerId, area) shouldBeRight Unit

        field.getCells()[area.from.y][area.from.x].playerId shouldBe playerId
        field.getCells()[area.from.y][area.to.x].playerId shouldBe playerId
        field.getCells()[area.to.y][area.from.x].playerId shouldBe playerId
        field.getCells()[area.to.y][area.to.x].playerId shouldBe playerId

        field.getCells().capturedCellCount() shouldBe (area.xDistance() + 1) * (area.yDistance() + 1) + 1
    }

    @Test
    fun `capture - inaccessible area when cell already captured`() {
        val area = area()

        checkCaptureWithInaccessibleArea(area, area.from.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.from.x, area.to.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.to.y)
    }

    private fun checkCaptureWithInaccessibleArea(area: Area, x: Int, y: Int) {
        val otherPlayerId = playerId()

        val cells = cells()
        cells.setCell(otherPlayerId, x, y)

        val field = field(cells = cells)

        field.capture(playerId(), area) shouldBeLeft InaccessibleArea

        field.getCells().capturedCellCount() shouldBe 1
    }

    @Test
    fun `capture - inaccessible area when not touch own cell`() {
        val playerId = playerId()
        val area = area(distanceToEdges = 2)

        val cells = cells()
        cells.setCell(playerId, area.from.x - 2, area.from.y)
        cells.setCell(playerId, area.from.x, area.from.y - 2)
        cells.setCell(playerId, area.to.x + 2, area.to.y)
        cells.setCell(playerId, area.to.x, area.to.y + 2)

        val field = field(cells = cells)

        field.capture(playerId, area) shouldBeLeft InaccessibleArea
    }
}
