package io.github.pavlentygood.cellcapture.game.app.integration

import io.github.pavlentygood.cellcapture.game.app.input.rest.CaptureCellsEndpoint
import io.github.pavlentygood.cellcapture.game.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.game.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.game.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.game.domain.*
import io.github.pavlentygood.cellcapture.game.restapi.API_V1_PLAYERS_CELLS
import io.github.pavlentygood.cellcapture.game.restapi.CaptureCellsApi
import io.github.pavlentygood.cellcapture.kernel.common.mapper
import io.github.pavlentygood.cellcapture.kernel.common.with
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.partyId
import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest(classes = [CaptureCellsEndpoint::class])
@Import(IntegrationConfig::class)
internal class CaptureCellsEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getPartyByPlayer: GetPartyByPlayer
    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `capture cells`() {
        val player = player()
        val nextPlayer = player()
        val dices = dices(
            firstValue = 3,
            secondValue = 2
        )
        val point = point(
            x = 5,
            y = 4
        )
        val cells = cells()
        cells.setCell(
            playerId = player.id,
            x = point.x,
            y = point.y
        )
        val party = party(
            owner = nextPlayer,
            currentPlayer = player,
            dices = dices,
            field = field(cells)
        )
        saveParty(party)
        val area = area(
            fromX = point.x + 1,
            fromY = point.y,
            toX = (point.x + 1) + dices.firstValue - 1,
            toY = point.y + dices.secondValue - 1
        )

        captureCells(player.id, area).andExpect {
            status { isOk() }
        }

        val storedParty = getPartyByPlayer(player.id)!!
        storedParty.currentPlayerId shouldBe nextPlayer.id
        storedParty.cells.capturedCells() shouldContainExactly listOf(
            cell(player.id, point.x, point.y),
            cell(player.id, point.x + 1, point.y),
            cell(player.id, point.x + 2, point.y),
            cell(player.id, point.x + 3, point.y),
            cell(player.id, point.x + 1, point.y + 1),
            cell(player.id, point.x + 2, point.y + 1),
            cell(player.id, point.x + 3, point.y + 1),
        )
    }

    @Test
    fun `party not found`() {
        captureCells(playerId(), area()).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `player not current`() {
        val currentPlayer = player()
        val otherPlayer = player()
        val party = party(
            currentPlayer = currentPlayer,
            otherPlayers = listOf(currentPlayer, otherPlayer)
        )
        saveParty(party)

        captureCells(otherPlayer.id, area()).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("PLAYER_NOT_CURRENT") }
            }
        }
    }

    @Test
    fun `dices not rolled`() {
        val player = player()
        val party = party(
            currentPlayer = player,
            dices = Dices.notRolled()
        )
        saveParty(party)

        captureCells(player.id, area()).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("DICES_NOT_ROLLED") }
            }
        }
    }

    @Test
    fun `mismatched area`() {
        val player = player()
        val dices = dices(
            firstValue = 3,
            secondValue = 2
        )
        val party = party(
            currentPlayer = player,
            dices = dices
        )
        saveParty(party)
        val area = area(
            fromX = 1,
            fromY = 1,
            toX = 4,
            toY = 4
        )

        captureCells(player.id, area).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("MISMATCHED_AREA") }
            }
        }
    }

    @Test
    fun `inaccessible area`() {
        val player = player()
        val dices = dices(
            firstValue = 1,
            secondValue = 1
        )
        val point = point(
            x = 2,
            y = 3
        )
        val cells = cells()
        cells.setCell(
            playerId = player.id,
            x = point.x,
            y = point.y
        )
        val party = party(
            currentPlayer = player,
            dices = dices,
            field = field(cells)
        )
        saveParty(party)
        val area = area(
            fromX = point.x,
            fromY = point.y,
            toX = point.x,
            toY = point.y
        )

        captureCells(player.id, area).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("INACCESSIBLE_AREA") }
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

        captureCells(player.id, area()).andExpect {
            status { isUnprocessableEntity() }
            content {
                jsonPath("$.type") { value("PARTY_COMPLETED") }
            }
        }
    }

    private fun captureCells(playerId: PlayerId, area: Area) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_CELLS.with("playerId", playerId.toInt())
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(
                CaptureCellsApi.Request(
                    first = CaptureCellsApi.Request.Point(area.from.x, area.from.y),
                    second = CaptureCellsApi.Request.Point(area.to.x, area.to.y)
                )
            )
        }
}
