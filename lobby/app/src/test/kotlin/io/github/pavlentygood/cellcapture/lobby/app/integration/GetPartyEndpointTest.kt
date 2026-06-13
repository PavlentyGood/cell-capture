package io.github.pavlentygood.cellcapture.lobby.app.integration

import io.github.pavlentygood.cellcapture.kernel.common.with
import io.github.pavlentygood.cellcapture.kernel.domain.partyId
import io.github.pavlentygood.cellcapture.lobby.app.input.rest.GetPartyEndpoint
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.lobby.domain.party
import io.github.pavlentygood.cellcapture.lobby.domain.player
import io.github.pavlentygood.cellcapture.lobby.restapi.API_V1_PARTY_BY_ID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@SpringBootTest(classes = [GetPartyEndpoint::class])
@Import(IntegrationConfig::class)
class GetPartyEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `get party`() {
        val owner = player()
        val partyId = partyId()
        val party = party(
            id = partyId,
            owner = owner
        )
        saveParty(party)

        mockMvc.get(API_V1_PARTY_BY_ID.with("partyId", partyId.toUUID()))
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id") { value(partyId.toUUID().toString()) }
                    jsonPath("$.started") { value(party.started) }
                    jsonPath("$.ownerId") { value(party.ownerId.toInt()) }
                    jsonPath("$.playerLimit") { value(party.playerLimit.value) }
                    jsonPath("$.players[0].id") { value(owner.id.toInt()) }
                    jsonPath("$.players[0].name") { value(owner.name.toStringValue()) }
                }
            }
    }

    @Test
    fun `party not found`() {
        mockMvc.get(API_V1_PARTY_BY_ID.with("partyId", partyId().toUUID()))
            .andExpect { status { isNotFound() } }
    }
}
