package ru.pavlentygood.cellcapture.app.component

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.domain.*
import ru.pavlentygood.cellcapture.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.rest.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getPartyFromDatabase: GetPartyFromDatabase
    @Autowired
    lateinit var savePartyToDatabase: SavePartyToDatabase

    @Test
    fun `start party`() {
        val (partyId, ownerId) = createParty()
        joinPlayer(partyId)
        startParty(ownerId)

        val party = getPartyFromDatabase.parties[PartyId(partyId)]!!
        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `capture cells`() {
        val currentPlayer = player(owner = true)

        val dice = dice(1)
        val dicePair = DicePair(
            first = dice,
            second = dice
        )

        val cells = cells()
        cells[3][1] = currentPlayer.id

        val party = party(
            owner = currentPlayer,
            otherPlayers = listOf(player()),
            status = Party.Status.STARTED,
            dicePair = dicePair,
            field = field(cells)
        )

        savePartyToDatabase(party)

        captureCells(currentPlayer.id.toInt())

        party.getCells().capturedCellCount() shouldBe 2
    }

    private fun createParty() =
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(playerName().toStringValue()))
        }.andExpect { status { isCreated() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, CreatePartyResponse::class.java) }
            .let { Pair(it.id, it.ownerId) }

    private fun joinPlayer(partyId: UUID) =
        mockMvc.post(API_V1_PARTIES_PLAYERS.with("partyId", partyId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName().toStringValue()))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, JoinPlayerResponse::class.java) }
            .id

    private fun startParty(playerId: Int) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toString())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isOk() } }

    private fun captureCells(playerId: Int) =
        mockMvc.post(API_V1_PLAYERS_CELLS.with("playerId", playerId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(
                CaptureCellsEndpoint.Request(
                    first = CaptureCellsEndpoint.Request.Point(x = 2, y = 3),
                    second = CaptureCellsEndpoint.Request.Point(x = 2, y = 3)
                )
            )
        }.andExpect { status { isOk() } }
}
