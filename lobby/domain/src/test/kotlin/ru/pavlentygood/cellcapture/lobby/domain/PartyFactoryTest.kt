package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.get

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val owner = player()
        val generatePlayerId = { owner.id }
        val partyFactory = PartyFactory(generatePlayerId)

        val party: Party = partyFactory.create(owner.name)

        party.started shouldBe false
        party.playerLimit shouldBe PlayerLimit.from(DEFAULT_PLAYER_LIMIT).get()
        party.ownerId shouldBe owner.id
        party.getPlayers() shouldBe listOf(owner)
    }
}
