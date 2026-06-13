package io.github.pavlentygood.cellcapture.game.domain

import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FieldTest {

    @Test
    fun `capture - inaccessible area because selected cell already captured`() {
        val area = area()

        checkCaptureWithInaccessibleArea(area, area.from.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.from.x, area.to.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.to.y)
    }

    private fun checkCaptureWithInaccessibleArea(area: Area, x: Int, y: Int) {
        val otherPlayerId = playerId()

        val cells = cells(
            captured = listOf(Cell(otherPlayerId, x, y))
        )

        val field = field(cells = cells)

        field.capture(playerId(), area) shouldBeLeft InaccessibleArea

        field.getCells().capturedCellCount() shouldBe 1
    }

    @Test
    fun `capture - inaccessible area because not touch own cell`() {
        val playerId = playerId()
        val area = area(distanceToEdges = 2)
        val cells = cells(
            captured = listOf(
                Cell(playerId, area.from.x - 2, area.from.y),
                Cell(playerId, area.from.x, area.from.y - 2),
                Cell(playerId, area.to.x + 2, area.to.y),
                Cell(playerId, area.to.x, area.to.y + 2)
            )
        )
        val field = field(cells = cells)

        field.capture(playerId, area) shouldBeLeft InaccessibleArea
    }
}
