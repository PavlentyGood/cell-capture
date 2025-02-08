package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RestorePartyTest {

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun `restore party`(partyCompleted: Boolean) {
        val party = party()
        val restoreParty = RestoreParty()

        val restoredParty = restoreParty(
            id = party.id,
            completed = partyCompleted,
            dices = party.dices,
            cells = party.cells,
            players = party.players,
            currentPlayerId = party.currentPlayerId,
            ownerId = party.ownerId
        ).shouldBeRight()

        restoredParty.apply {
            id shouldBe party.id
            dices shouldBe party.dices
            cells shouldBe party.cells
            players shouldBe party.players
            currentPlayerId shouldBe party.currentPlayerId
            ownerId shouldBe party.ownerId
        }
    }
}
