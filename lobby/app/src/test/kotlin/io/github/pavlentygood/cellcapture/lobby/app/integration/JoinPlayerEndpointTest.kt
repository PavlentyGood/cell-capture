package io.github.pavlentygood.cellcapture.lobby.app.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.pavlentygood.cellcapture.kernel.domain.partyId
import io.github.pavlentygood.cellcapture.kernel.domain.playerName
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.lobby.domain.party
import io.github.pavlentygood.cellcapture.lobby.domain.player
import io.github.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES_PLAYERS
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import io.github.pavlentygood.cellcapture.lobby.rest.endpoint.JoinPlayerEndpoint
import io.github.pavlentygood.cellcapture.lobby.rest.endpoint.with
import io.github.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import io.github.pavlentygood.cellcapture.lobby.usecase.port.SaveParty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest(classes = [JoinPlayerEndpoint::class])
@Import(IntegrationConfig::class)
class JoinPlayerEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    lateinit var saveParty: SaveParty
    @Autowired
    lateinit var getParty: GetParty

    @Test
    fun `join player`() {
        val playerName = playerName()
        val partyId = partyId()
        val party = party(
            id = partyId
        )
        saveParty(party)

        val result = joinPlayer(partyId.toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(JoinPlayerRequest(name = playerName.toStringValue()))
        }

        val storedParty = getParty(partyId)!!
        val joinedPlayer = storedParty.getPlayers().single { it.id != party.ownerId }
        joinedPlayer.name shouldBe playerName

        result.andExpect {
            status { isOk() }
            content {
                jsonPath("$.id") { value(joinedPlayer.id.toInt()) }
            }
        }
    }

    @Test
    fun `party not found`() {
        joinPlayer(partyId().toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(JoinPlayerRequest(name = playerName().toStringValue()))
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `illegal party id`() {
        joinPlayer("illegal-party-id") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `illegal player name`() {
        val illegalName = ""

        joinPlayer(partyId().toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(JoinPlayerRequest(name = illegalName))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `party already started`() {
        val partyId = partyId()
        val party = party(
            id = partyId,
            started = true,
            otherPlayers = listOf(player())
        )
        saveParty(party)

        joinPlayer(partyId.toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(JoinPlayerRequest(name = playerName().toStringValue()))
        }.andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `player count limit reached`() {
        val partyId = partyId()
        val party = party(
            id = partyId,
            owner = player(),
            otherPlayers = listOf(player()),
            playerLimit = 2
        )
        saveParty(party)

        joinPlayer(partyId.toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(JoinPlayerRequest(name = playerName().toStringValue()))
        }.andExpect {
            status { isUnprocessableEntity() }
        }
    }

    private fun joinPlayer(partyId: Any, dsl: MockHttpServletRequestDsl.() -> Unit) =
        mockMvc.post(
            urlTemplate = API_V1_PARTIES_PLAYERS.with("partyId", partyId),
            dsl = dsl
        )
}
