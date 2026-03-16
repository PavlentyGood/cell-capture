package ru.pavlentygood.cellcapture.lobby.app.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.kernel.domain.version
import ru.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import ru.pavlentygood.cellcapture.lobby.domain.player
import ru.pavlentygood.cellcapture.lobby.domain.playerLimit
import ru.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.CreatePartyEndpoint
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(classes = [CreatePartyEndpoint::class])
@Import(IntegrationConfig::class)
class CreatePartyEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    lateinit var getParty: GetParty

    @Test
    fun `create party`() {
        val ownerName = playerName()

        val result = mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(CreatePartyRequest(ownerName.toStringValue()))
        }

        val body = result.andReturn().response.contentAsString
        val partyUuid = UUID.fromString(JsonPath.parse(body).read("$.id"))
        val partyId = partyId(partyUuid)
        val ownerIdInt = JsonPath.parse(body).read<Int>("$.ownerId")
        val ownerId = playerId(ownerIdInt)

        result.andExpect {
            status { isCreated() }
            content {
                header {
                    string(HttpHeaders.LOCATION, "$API_V1_PARTIES/$partyUuid")
                }
                jsonPath("$.ownerId") { value(ownerIdInt) }
            }
        }

        val storedParty = getParty(partyId)!!
        storedParty.version shouldBe version(1)
        storedParty.started shouldBe false
        storedParty.ownerId shouldBe ownerId
        storedParty.playerLimit shouldBe playerLimit(4)
        storedParty.getPlayers() shouldContainExactly listOf(player(ownerId, ownerName))
    }

    @Test
    fun `illegal owner name`() {
        val illegalName = "Bo"
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(CreatePartyRequest(illegalName))
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
