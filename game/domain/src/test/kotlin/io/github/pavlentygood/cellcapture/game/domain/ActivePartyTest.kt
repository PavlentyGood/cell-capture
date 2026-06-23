package io.github.pavlentygood.cellcapture.game.domain

import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class ActivePartyTest {

    @Test
    fun `roll dices`() {
        val player = player()
        val party = party(
            dices = Dices.notRolled(),
            currentPlayer = player
        )

        val rolledDices = party.roll(player.id).shouldBeRight()

        party.popEvents() shouldContainExactly listOf(DicesRolledEvent(party.id, player.id, rolledDices))
        party.dices shouldBe rolledDices
        rolledDices shouldNotBe Dices.notRolled()
    }

    @Test
    fun `roll dices - player not current`() {
        val party = party()
        party.roll(playerId()) shouldBeLeft PlayerNotCurrent
        party.popEvents().isEmpty() shouldBe true
    }

    @Test
    fun `roll dices - dices already rolled`() {
        val player = player()
        val dices = dices()
        val party = party(
            dices = dices,
            currentPlayer = player
        )

        party.roll(player.id) shouldBeLeft DicesAlreadyRolled

        party.popEvents().isEmpty() shouldBe true
        party.dices shouldBe dices
    }

    @Test
    fun `capture cells`() {
        val player = player()
        val nextPlayer = player()
        val area = area(fromX = 1, fromY = 1, toX = 3, toY = 2)
        val dices = dices(area.xDistance() + 1, area.yDistance() + 1)
        val cells = cells(
            captured = listOf(Cell(player.id, area.from.x - 1, area.from.y))
        )
        val field = field(cells = cells)
        val party = party(
            dices = dices,
            field = field,
            currentPlayer = player,
            otherPlayers = listOf(player, nextPlayer)
        )

        party.capture(player.id, area) shouldBeRight Unit

        party.popEvents() shouldContainExactly listOf(CellsCapturedEvent(party.id, player.id, area))
        party.dices shouldBe Dices.notRolled()
        party.currentPlayerId shouldBe nextPlayer.id
        party.cells[area.from.y][area.from.x].playerId shouldBe player.id
        party.cells[area.from.y][area.to.x].playerId shouldBe player.id
        party.cells[area.to.y][area.from.x].playerId shouldBe player.id
        party.cells[area.to.y][area.to.x].playerId shouldBe player.id
        party.cells.capturedCellCount() shouldBe (area.xDistance() + 1) * (area.yDistance() + 1) + 1
    }

    @Test
    fun `capture cells - player not current`() {
        val area = area()
        val party = party()

        party.capture(playerId(), area) shouldBeLeft PlayerNotCurrent
        party.popEvents().isEmpty() shouldBe true
    }

    @Test
    fun `capture cells - dices not rolled`() {
        val area = area()
        val player = player()
        val party = party(
            dices = Dices.notRolled(),
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft DicesNotRolled
        party.popEvents().isEmpty() shouldBe true
    }

    @Test
    fun `capture cells - mismatched area`() {
        val area = area()
        val dices = dices()
        val player = player()

        val party = party(
            dices = dices,
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft MismatchedArea
        party.popEvents().isEmpty() shouldBe true
    }

    @Test
    fun `capture cells - inaccessible area because selected cell already captured`() {
        val area = area(fromX = 1, fromY = 1, toX = 3, toY = 2)
        val dices = dices(area.xDistance() + 1, area.yDistance() + 1)
        val player = player()
        val cells = cells(
            captured = listOf(Cell(player.id, area.from.x, area.from.y))
        )
        val field = field(cells = cells)
        val party = party(
            dices = dices,
            field = field,
            currentPlayer = player
        )

        party.capture(player.id, area) shouldBeLeft InaccessibleArea
        party.popEvents().isEmpty() shouldBe true
    }
}
