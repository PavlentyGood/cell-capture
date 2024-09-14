package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val ownerId = playerId()
        val ownerName = playerName()

        val generatePlayerId = mockk<GeneratePlayerId>()
        every { generatePlayerId() } returns ownerId

        val partyFactory = PartyFactory(generatePlayerId)

        val party: Party = partyFactory.create(ownerName)

        party.playerLimit shouldBe PlayerLimit.from(DEFAULT_PLAYER_LIMIT).getOrNull()!!
        party.status shouldBe Party.Status.NEW
        party.ownerId shouldBe party.getPlayers().single().id

        party.getPlayers().single().also {
            it.id shouldBe ownerId
            it.name shouldBe ownerName
            it.owner shouldBe true
        }
    }
}
