package ru.pavlentygood.cellcapture.party.domain

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val owner = player()
        val players = listOf(owner, player())
        val partyInfo = partyInfo(
            ownerId = owner.id,
            players = players
        )
        val partyFactory = PartyFactory()

        val party: Party = partyFactory.create(partyInfo)

        party.completed shouldBe false
        party.dicePair shouldBe null
        party.ownerId shouldBe owner.id
        party.currentPlayerId shouldBe owner.id
        party.players shouldContainExactly players
        party.getCells() shouldHaveSize Field.HEIGHT
        party.getCells()[0] shouldHaveSize Field.WIDTH
        party.getCells()[0][0] shouldBe Field.nonePlayerId
    }
}
