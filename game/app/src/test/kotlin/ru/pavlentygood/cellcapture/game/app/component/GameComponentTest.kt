package ru.pavlentygood.cellcapture.game.app.component

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.TestPersistenceConfig
import ru.pavlentygood.cellcapture.game.rest.*
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@SpringBootTest
@AutoConfigureMockMvc
@Import(value = [TestPersistenceConfig::class])
class GameComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var createParty: CreatePartyUseCase
    @Autowired
    lateinit var getPartyByPlayer: GetPartyByPlayerFromDatabase
    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @RepeatedTest(100)
    fun `all use cases as process`() {
        val party = createParty() // use case

        val ownerId = party.ownerId
        val startCellCount = party.cells.capturedCellCount()
        val startCell = party.cells.findStartCell(ownerId)

        val dices = roll(ownerId) // use case

        captureCells(ownerId, dices, startCell) // use case

        val partyAfterCapture = getPartyByPlayer(ownerId)!!
        val expectedCapturedCellCount = startCellCount + dices.first * dices.second

        partyAfterCapture.cells.capturedCellCount() shouldBe expectedCapturedCellCount
    }

    private fun createParty(): Party =
        generateSequence {
            clearDatabase()
            val partyInfo = partyInfo()
            createParty(partyInfo)
            getPartyByPlayer(partyInfo.ownerId)
        }.first { party ->
            party.isStartCellFarFromSides() && party.isStartCellFarFromCapturedCells()
        }

    private fun clearDatabase() {
        jdbcTemplate.update("truncate table parties cascade", mapOf<String, Any>())
    }

    private fun roll(playerId: PlayerId) =
        mockMvc.post(API_V1_PLAYERS_DICES.with("playerId", playerId.toInt()))
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, RollEndpoint.RollResponse::class.java) }
            .dices

    private fun captureCells(
        playerId: PlayerId,
        dices: RollEndpoint.DicesResponse,
        startCell: Cell,
    ) {
        val x1 = startCell.x + 1
        val x2 = x1 + dices.first - 1
        val y1 = startCell.y
        val y2 = y1 + dices.second - 1
        val request = CaptureCellsEndpoint.Request(
            first = CaptureCellsEndpoint.Request.Point(x = x1, y = y1),
            second = CaptureCellsEndpoint.Request.Point(x = x2, y = y2)
        )
        mockMvc.post(API_V1_PLAYERS_CELLS.with("playerId", playerId.toInt())) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect { status { isOk() } }
    }

    private fun Party.isStartCellFarFromSides(): Boolean {
        fun Cell.isFarFromRightSide() =
            this.x + 1 + Dice.MAX < Field.WIDTH

        fun Cell.isFarFromBottomSide() =
            this.y + 1 + Dice.MAX < Field.HEIGHT

        val cell = cells.findStartCell(ownerId)
        return cell.isFarFromRightSide() && cell.isFarFromBottomSide()
    }

    private fun Party.isStartCellFarFromCapturedCells(): Boolean {
        val cell = cells.findStartCell(ownerId)
        return cells.capturedCells().none {
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

    private fun Array<Array<PlayerId>>.findStartCell(forPlayer: PlayerId) =
        capturedCells()
            .single { it.playerId == forPlayer }

    data class Cell(
        val playerId: PlayerId,
        val x: Int,
        val y: Int,
    )
}
