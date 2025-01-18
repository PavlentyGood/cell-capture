package ru.pavlentygood.cellcapture.party.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FieldTest {

    @Test
    fun `appoint start cells`() {
        val maxCellCount = ru.pavlentygood.cellcapture.party.domain.Field.Companion.WIDTH * ru.pavlentygood.cellcapture.party.domain.Field.Companion.HEIGHT
        val field = ru.pavlentygood.cellcapture.party.domain.field()
        val playerIds = generateSequence { ru.pavlentygood.cellcapture.party.domain.playerId(until = 1000000) }
            .take(maxCellCount)
            .toList()

        field.appointStartCells(playerIds)

        field.getCells().capturedCellCount() shouldBe maxCellCount
        field.getCells().flatMap { it.map { id -> id } } shouldContainAll playerIds
    }

    @Test
    fun `capture cells`() {
        val playerId = ru.pavlentygood.cellcapture.party.domain.playerId()
        val area = ru.pavlentygood.cellcapture.party.domain.area(distanceToEdges = 1)

        val cells = ru.pavlentygood.cellcapture.party.domain.cells()
        cells[area.from.y][area.from.x - 1] = playerId

        val field = ru.pavlentygood.cellcapture.party.domain.field(cells = cells)

        field.capture(playerId, area) shouldBeRight Unit

        field.getCells()[area.from.y][area.from.x] shouldBe playerId
        field.getCells()[area.from.y][area.to.x] shouldBe playerId
        field.getCells()[area.to.y][area.from.x] shouldBe playerId
        field.getCells()[area.to.y][area.to.x] shouldBe playerId

        field.getCells().capturedCellCount() shouldBe (area.xDistance() + 1) * (area.yDistance() + 1) + 1
    }

    @Test
    fun `capture - inaccessible area when cell already captured`() {
        val area = ru.pavlentygood.cellcapture.party.domain.area()

        checkCaptureWithInaccessibleArea(area, area.from.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.from.x, area.to.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.from.y)
        checkCaptureWithInaccessibleArea(area, area.to.x, area.to.y)
    }

    private fun checkCaptureWithInaccessibleArea(area: ru.pavlentygood.cellcapture.party.domain.Area, x: Int, y: Int) {
        val otherPlayerId = ru.pavlentygood.cellcapture.party.domain.playerId()

        val cells = ru.pavlentygood.cellcapture.party.domain.cells()
        cells[y][x] = otherPlayerId

        val field = ru.pavlentygood.cellcapture.party.domain.field(cells = cells)

        field.capture(ru.pavlentygood.cellcapture.party.domain.playerId(), area) shouldBeLeft ru.pavlentygood.cellcapture.party.domain.Party.InaccessibleArea

        field.getCells().capturedCellCount() shouldBe 1
    }

    @Test
    fun `capture - inaccessible area when not touch own cell`() {
        val playerId = ru.pavlentygood.cellcapture.party.domain.playerId()
        val area = ru.pavlentygood.cellcapture.party.domain.area(distanceToEdges = 2)

        val cells = ru.pavlentygood.cellcapture.party.domain.cells()
        cells[area.from.y][area.from.x - 2] = playerId
        cells[area.from.y - 2][area.from.x] = playerId
        cells[area.to.y][area.to.x + 2] = playerId
        cells[area.to.y + 2][area.to.x] = playerId

        val field = ru.pavlentygood.cellcapture.party.domain.field(cells = cells)

        field.capture(playerId, area) shouldBeLeft ru.pavlentygood.cellcapture.party.domain.Party.InaccessibleArea
    }
}
