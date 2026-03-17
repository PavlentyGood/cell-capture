package io.github.pavlentygood.cellcapture.game.app.integration

import io.github.pavlentygood.cellcapture.game.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.game.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.game.domain.Dices
import io.github.pavlentygood.cellcapture.game.domain.completedParty
import io.github.pavlentygood.cellcapture.game.domain.party
import io.github.pavlentygood.cellcapture.game.domain.player
import io.github.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_DICES
import io.github.pavlentygood.cellcapture.game.rest.endpoint.RollDicesEndpoint
import io.github.pavlentygood.cellcapture.game.rest.endpoint.with
import io.github.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.partyId
import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

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
            content {
                jsonPath("$.type") { value("PLAYER_NOT_CURRENT") }
            }
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
            content {
                jsonPath("$.type") { value("DICES_ALREADY_ROLLED") }
            }
        }
    }

    @Test
    fun `party completed`() {
        val player = player()
        val party = completedParty(
            id = partyId(),
            currentPlayer = player
        )
        saveParty(party)

        rollDices(player.id).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("PARTY_COMPLETED") }
            }
        }
    }

    private fun rollDices(playerId: PlayerId) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_DICES.with("playerId", playerId.toInt())
        )
}
