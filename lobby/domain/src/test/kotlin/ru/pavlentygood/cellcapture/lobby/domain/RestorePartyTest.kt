package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RestorePartyTest {

    @Test
    fun `restore party`() {
        val party = party()
        val restoreParty = RestoreParty()

        val restoredParty: Party = restoreParty(
            id = party.id,
            started = party.started,
            playerLimit = party.playerLimit,
            players = party.players,
            ownerId = party.ownerId
        )

        restoredParty.apply {
            id shouldBe party.id
            started shouldBe party.started
            playerLimit shouldBe party.playerLimit
            players shouldBe party.players
            ownerId shouldBe party.ownerId
        }
    }
}
