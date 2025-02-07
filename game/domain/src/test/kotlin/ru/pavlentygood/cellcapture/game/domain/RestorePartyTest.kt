package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.partyId

class RestorePartyTest {

    @Test
    fun `restore party`() {
        val partyId = partyId()
        val dices = dices()
        val cells = cells()
        val owner = player()
        val currentPlayer = player()
        val players = listOf(owner, currentPlayer)
        val restoreParty = RestoreParty()

        val restoredParty = restoreParty(
            id = partyId,
            completed = false,
            dices = dices,
            cells = cells,
            players = players,
            currentPlayerId = currentPlayer.id,
            ownerId = owner.id
        ).shouldBeRight()

        (restoredParty as Party).let {
            it.id shouldBe partyId
            it.dices shouldBe dices
            it.getCells() shouldBe cells
            it.getPlayers() shouldBe players
            it.currentPlayerId shouldBe currentPlayer.id
            it.ownerId shouldBe owner.id
        }
    }

    @Test
    fun `restore completed party`() {
        val partyId = partyId()
        val dices = dices()
        val cells = cells()
        val owner = player()
        val currentPlayer = player()
        val players = listOf(owner, currentPlayer)
        val restoreParty = RestoreParty()

        val restoredParty = restoreParty(
            id = partyId,
            completed = true,
            dices = dices,
            cells = cells,
            players = players,
            currentPlayerId = currentPlayer.id,
            ownerId = owner.id
        ).shouldBeRight()

        (restoredParty as CompletedParty).id shouldBe partyId
    }
}
