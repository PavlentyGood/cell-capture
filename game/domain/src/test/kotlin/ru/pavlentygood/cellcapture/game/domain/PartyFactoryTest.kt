package ru.pavlentygood.cellcapture.game.domain

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val partyInfo = partyInfo()
        val partyFactory = PartyFactory()

        val party: Party = partyFactory.create(partyInfo)

        party.id shouldBe partyInfo.partyId
        party.completed shouldBe false
        party.dicePair shouldBe null
        party.ownerId shouldBe partyInfo.ownerId
        party.currentPlayerId shouldBe partyInfo.ownerId
        party.players shouldContainExactly partyInfo.players
        party.getCells() shouldHaveSize Field.HEIGHT
        party.getCells()[0] shouldHaveSize Field.WIDTH
        party.getCells()[0][0] shouldBe Field.nonePlayerId
    }
}
