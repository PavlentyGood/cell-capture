package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyTest {

    @Test
    fun `join player`() {
        val partyId = partyId()
        val playerId = playerId()
        val name = playerName()
        val party = party(partyId)

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeRight playerId

        party.getPlayers().map { it.id } shouldContainOnly listOf(playerId)
    }

    @Test
    fun `join player - limit`() {
        val partyId = partyId()
        val playerId = playerId()
        val name = playerName()
        val party = party(
            id = partyId,
            playerLimit = 0
        )

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeLeft PlayerCountLimitExceeded

        party.getPlayers().size shouldBe 0
    }
}
