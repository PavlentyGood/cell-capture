package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val owner = player()

        val generatePlayerId = mockk<GeneratePlayerId>()
        every { generatePlayerId() } returns owner.id

        val partyFactory = PartyFactory(generatePlayerId)

        val party: Party = partyFactory.create(owner.name)

        party.started shouldBe false
        party.playerLimit shouldBe PlayerLimit.from(DEFAULT_PLAYER_LIMIT).get()
        party.ownerId shouldBe owner.id
        party.players shouldBe listOf(owner)
    }
}
