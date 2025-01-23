package ru.pavlentygood.cellcapture.lobby.app.component

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.lobby.rest.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class LobbyComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getPartyFromDatabase: GetPartyFromDatabase

    @RepeatedTest(10)
    fun `test all scenarios together`() {
        val ownerName = playerName()
        val playerName = playerName()

        val created: CreatePartyResponse = createParty(ownerName)
        joinPlayer(created.id, playerName)
        startParty(created.ownerId)

        val party: Party = getPartyFromDatabase(created.id)

        party.started shouldBe true
        party.players.size shouldBe 2
        party.players.map { it.id.toInt() } shouldContain created.ownerId
        party.players.map { it.name } shouldContainExactly listOf(ownerName, playerName)
    }

    private fun createParty(playerName: PlayerName) =
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(playerName.toStringValue()))
        }.andExpect { status { isCreated() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, CreatePartyResponse::class.java) }

    private fun joinPlayer(partyId: UUID, playerName: PlayerName) =
        mockMvc.post(API_V1_PARTIES_PLAYERS.with("partyId", partyId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName.toStringValue()))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, JoinPlayerResponse::class.java) }
            .id

    private fun startParty(playerId: Int) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toString())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isOk() } }

    private fun getPartyFromDatabase(partyId: UUID) =
        getPartyFromDatabase.parties[PartyId(partyId)]!!
}
