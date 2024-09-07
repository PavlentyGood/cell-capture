package ru.pavlentygood.cellcapture.app.component

import io.kotest.matchers.collections.shouldContainOnly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.playerName
import ru.pavlentygood.cellcapture.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.rest.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class JoinPlayerToPartyComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getPartyFromDatabase: GetPartyFromDatabase

    @Test
    fun `join player to party`() {
        val partyId = createParty()
        val playerId = joinPlayer(partyId)

        getPartyFromDatabase.parties[PartyId(partyId)]!!
            .getPlayers().map { it.id.toInt() } shouldContainOnly listOf(playerId)
    }

    private fun createParty() =
        mockMvc.post(API_V1_PARTIES)
            .andExpect { status { isCreated() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, CreatePartyResponse::class.java) }
            .id

    private fun joinPlayer(partyId: UUID) =
        mockMvc.post(API_V1_PARTIES_PLAYERS.with("partyId", partyId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName().toStringValue()))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, JoinPlayerResponse::class.java) }
            .id
}
