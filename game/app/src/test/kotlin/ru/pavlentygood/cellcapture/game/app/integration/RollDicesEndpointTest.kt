package ru.pavlentygood.cellcapture.game.app.integration

import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.game.app.integration.config.IntegrationConfig
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_DICES
import ru.pavlentygood.cellcapture.game.rest.endpoint.RollDicesEndpoint
import ru.pavlentygood.cellcapture.game.rest.endpoint.with
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.version

@AutoConfigureMockMvc
@SpringBootTest(classes = [RollDicesEndpoint::class])
@Import(IntegrationConfig::class)
internal class RollDicesEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getPartyByPlayer: GetPartyByPlayer
    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `roll dices`() {
        val player = player()
        val nextPlayer = player()
        val party = party(
            owner = player,
            currentPlayer = player,
            otherPlayers = listOf(nextPlayer),
            dices = Dices.notRolled()
        )
        saveParty(party)

        val result = rollDices(player.id)

        val storedParty = getPartyByPlayer(player.id)!!
        storedParty.version shouldBe party.version.next()
        storedParty.dices.firstValue!! shouldBeInRange 1..6
        storedParty.dices.secondValue!! shouldBeInRange 1..6

        result.andExpect {
            status { isOk() }
            content {
                jsonPath("$.dices.first") { value(storedParty.dices.firstValue) }
                jsonPath("$.dices.second") { value(storedParty.dices.secondValue) }
            }
        }
    }

    @Test
    fun `party not found`() {
        rollDices(playerId()).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `player not current`() {
        val player = player()
        val nextPlayer = player()
        val party = party(
            owner = player,
            currentPlayer = player,
            otherPlayers = listOf(nextPlayer),
            dices = Dices.notRolled()
        )
        saveParty(party)

        rollDices(nextPlayer.id).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `dices already rolled`() {
        val player = player()
        val party = party(
            currentPlayer = player,
            dices = Dices.roll()
        )
        saveParty(party)

        rollDices(player.id).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `party completed`() {
        val owner = player()
        val player = player()
        val party = CompletedParty(
            id = partyId(),
            version = version(),
            dices = Dices.notRolled(),
            cells = cells(),
            ownerId = owner.id,
            currentPlayerId = player.id,
            players = listOf(owner, player)
        )
        saveParty(party)

        rollDices(player.id).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    private fun rollDices(playerId: PlayerId) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_DICES.with("playerId", playerId.toInt())
        )
}
