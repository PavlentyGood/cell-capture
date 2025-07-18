package ru.pavlentygood.cellcapture.game.app.component

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_PARTY
import ru.pavlentygood.cellcapture.game.rest.endpoint.GetPartyEndpoint
import ru.pavlentygood.cellcapture.game.rest.endpoint.with
import ru.pavlentygood.cellcapture.game.usecase.GetPartyByPlayerUseCase
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.playerId

@SpringBootTest(classes = [GetPartyEndpointTest::class, GetPartyEndpoint::class])
@EnableAutoConfiguration
@AutoConfigureMockMvc
@Import(
    value = [
        GetPartyByPlayerUseCase::class,
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        RestoreParty::class
    ]
)
class GetPartyEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `get party by player`() {
        val owner = player()
        val dices = dices()
        val cells = cells()
        val cell = Cell(
            playerId = owner.id,
            x = 2,
            y = 1
        )
        cells[1][2] = cell
        val party = party(
            owner = owner,
            dices = dices,
            field = field(cells)
        )
        saveParty(party)

        mockMvc.get(API_V1_PLAYERS_PARTY.with("playerId", owner.id.toInt()))
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id") { value(party.id.toUUID().toString()) }
                    jsonPath("$.completed") { value(party.completed) }
                    jsonPath("$.ownerId") { value(party.ownerId.toInt()) }
                    jsonPath("$.currentPlayerId") { value(party.currentPlayerId.toInt()) }
                    jsonPath("$.dices.first") { value(dices.firstValue) }
                    jsonPath("$.dices.second") { value(dices.secondValue) }
                    jsonPath("$.players[0].id") { value(owner.id.toInt()) }
                    jsonPath("$.players[0].name") { value(owner.name.toStringValue()) }
                    jsonPath("$.cells[0].playerId") { value(cell.playerId.toInt()) }
                    jsonPath("$.cells[0].x") { value(cell.x) }
                    jsonPath("$.cells[0].y") { value(cell.y) }
                }
            }
    }

    @Test
    fun `party not found`() {
        mockMvc.get(API_V1_PLAYERS_PARTY.with("playerId", playerId().toInt()))
            .andExpect { status { isNotFound() } }
    }
}
