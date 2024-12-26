package ru.pavlentygood.cellcapture.app.component

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.domain.*
import ru.pavlentygood.cellcapture.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.rest.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var getPartyFromDatabase: GetPartyFromDatabase

    @RepeatedTest(10)
    fun `test all scenarios`() {
        val party = generateSequence {
            val created = createParty()
            joinPlayer(created.id)
            startParty(created.ownerId)
            getPartyFromDatabase(created.id)
        }.first { party ->
            party.isStartCellFarFromSides() && party.isStartCellFarFromCapturedCells()
        }

        val partyId = party.id.toUUID()
        val ownerId = party.ownerId.toInt()

        val startCellCount = party.getCells().capturedCellCount()
        val startCell = party.getCells().findStartCell(ownerId)

        val dicePair = roll(ownerId)
        captureCells(ownerId, dicePair, startCell)

        val partyAfterCapture = getPartyFromDatabase(partyId)
        partyAfterCapture.getCells().capturedCellCount() shouldBe startCellCount + dicePair.first * dicePair.second
    }

    private fun createParty() =
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(playerName().toStringValue()))
        }.andExpect { status { isCreated() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, CreatePartyResponse::class.java) }

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

    private fun roll(playerId: Int) =
        mockMvc.post(API_V1_PLAYERS_DICES.with("playerId", playerId))
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, RollEndpoint.RollResponse::class.java) }
            .dicePair

    private fun captureCells(
        playerId: Int,
        dicePair: RollEndpoint.DicePairResponse,
        startCell: Cell,
    ) {
        val x1 = startCell.x + 1
        val x2 = x1 + dicePair.first - 1
        val y1 = startCell.y
        val y2 = y1 + dicePair.second - 1
        val request = CaptureCellsEndpoint.Request(
            first = CaptureCellsEndpoint.Request.Point(x = x1, y = y1),
            second = CaptureCellsEndpoint.Request.Point(x = x2, y = y2)
        )
        mockMvc.post(API_V1_PLAYERS_CELLS.with("playerId", playerId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect { status { isOk() } }
    }

    private fun getPartyFromDatabase(partyId: UUID) =
        getPartyFromDatabase.parties[PartyId(partyId)]!!

    private fun Party.isStartCellFarFromSides(): Boolean {
        fun Cell.isFarFromRightSide() =
            this.x + 1 + Dice.MAX < Field.WIDTH

        fun Cell.isFarFromBottomSide() =
            this.y + 1 + Dice.MAX < Field.HEIGHT

        val cell = getCells().findStartCell(ownerId.toInt())
        return cell.isFarFromRightSide() && cell.isFarFromBottomSide()
    }

    private fun Party.isStartCellFarFromCapturedCells(): Boolean {
        val cell = getCells().findStartCell(ownerId.toInt())
        return getCells().capturedCells().none {
            this.ownerId != it.playerId &&
                    cell.x + Dice.MAX >= it.x &&
                    cell.y + Dice.MAX >= it.y
        }
    }

    private fun Array<Array<PlayerId>>.capturedCells() =
        this.indices.map { y ->
            this[y].indices.mapNotNull { x ->
                if (this[y][x] != Field.nonePlayerId) {
                    Cell(this[y][x], x, y)
                } else {
                    null
                }
            }
        }.flatten()

    private fun Array<Array<PlayerId>>.findStartCell(forPlayerId: Int) =
        capturedCells()
            .single { it.playerId.toInt() == forPlayerId }

    data class Cell(
        val playerId: PlayerId,
        val x: Int,
        val y: Int,
    )
}
