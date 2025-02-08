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
            cells = party.getCells(),
            players = party.getPlayers(),
            currentPlayerId = party.currentPlayerId,
            ownerId = party.ownerId
        ).shouldBeRight()

        restoredParty.apply {
            id shouldBe party.id
            dices shouldBe party.dices
            getCells() shouldBe party.getCells()
            getPlayers() shouldBe party.getPlayers()
            currentPlayerId shouldBe party.currentPlayerId
            ownerId shouldBe party.ownerId
        }
    }
}
