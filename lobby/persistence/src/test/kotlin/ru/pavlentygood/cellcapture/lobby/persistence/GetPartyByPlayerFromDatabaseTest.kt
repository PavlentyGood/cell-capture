package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@JpaTest
@Import(
    value = [
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        MapPartyToDomain::class,
        RestoreParty::class
    ]
)
class GetPartyByPlayerFromDatabaseTest {

    @Autowired
    private lateinit var saveParty: SaveParty
    @Autowired
    private lateinit var getPartyByPlayer: GetPartyByPlayer

    @Test
    fun `get party by player`() {
        val player = player()
        val party = party(
            otherPlayers = listOf(player)
        )
        saveParty(party)

        getPartyByPlayer(player.id)!!.apply {
            id shouldBe party.id
            started shouldBe party.started
            ownerId shouldBe party.ownerId
            playerLimit shouldBe party.playerLimit
            getPlayers() shouldBe party.getPlayers()
        }
    }

    @Test
    fun `get party by player - not found`() {
        getPartyByPlayer(playerId()) shouldBe null
    }
}
