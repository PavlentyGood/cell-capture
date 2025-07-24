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
        party.dices shouldBe Dices.notRolled()
        party.ownerId shouldBe partyInfo.ownerId
        party.currentPlayerId shouldBe partyInfo.ownerId
        party.players shouldContainExactly partyInfo.players
        party.cells shouldHaveSize Field.HEIGHT
        party.cells[0] shouldHaveSize Field.WIDTH
        party.cells[0][0] shouldBe Cell(owner.id, 0, 0)
        party.cells[Field.HEIGHT - 1][Field.WIDTH - 1] shouldBe
                Cell(players[1].id, Field.WIDTH - 1, Field.HEIGHT - 1)
        party.cells[Field.HEIGHT / 4][0] shouldBe
                Cell(players[MAX_PLAYER_COUNT - 2].id, 0, Field.HEIGHT / 4)
        party.cells[Field.HEIGHT / 4 * 3][Field.WIDTH - 1] shouldBe
                Cell(players[MAX_PLAYER_COUNT - 1].id, Field.WIDTH - 1, Field.HEIGHT / 4 * 3)
        party.cells.capturedCellCount() shouldBe partyInfo.players.size
        party.cells.flatten().map { it.playerId } shouldContainAll partyInfo.playerList.playerIds
    }
}
