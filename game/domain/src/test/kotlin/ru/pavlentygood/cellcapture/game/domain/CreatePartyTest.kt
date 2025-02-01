package ru.pavlentygood.cellcapture.game.domain

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.MAX_PLAYER_COUNT

class CreatePartyTest {

    @Test
    fun `create party with max players`() {
        val players = generateSequence { player() }
            .take(MAX_PLAYER_COUNT)
            .toList()
        val owner = players.first()
        val partyInfo = partyInfo(
            ownerId = owner.id,
            players = players
        )
        val createParty = CreateParty()

        val party: Party = createParty(partyInfo)

        party.id shouldBe partyInfo.partyId
        party.completed shouldBe false
        party.dicePair shouldBe null
        party.ownerId shouldBe partyInfo.ownerId
        party.currentPlayerId shouldBe partyInfo.ownerId
        party.getPlayers() shouldContainExactly partyInfo.players
        party.getCells() shouldHaveSize Field.HEIGHT
        party.getCells()[0] shouldHaveSize Field.WIDTH
        party.getCells().capturedCellCount() shouldBe partyInfo.players.size
        party.getCells().flatMap { it.map { id -> id } } shouldContainAll partyInfo.playerList.playerIds
    }
}
