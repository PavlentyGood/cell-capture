package ru.pavlentygood.cellcapture.game.app.component

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.Point
import ru.pavlentygood.cellcapture.game.domain.capturedCellCount
import ru.pavlentygood.cellcapture.game.domain.point
import ru.pavlentygood.cellcapture.game.listening.*
import ru.pavlentygood.cellcapture.game.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.game.persistence.GetPartyByPlayerFromDatabase
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_CELLS
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_DICES
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi.Request
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.DicesResponse
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.RollResponse
import ru.pavlentygood.cellcapture.game.rest.endpoint.mapper
import ru.pavlentygood.cellcapture.game.rest.endpoint.with
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import kotlin.time.Duration.Companion.seconds

@SpringBootTest
@AutoConfigureMockMvc
@Import(value = [TestProducerConfig::class])
@ImportAutoConfiguration(exclude = [TestDatabaseAutoConfiguration::class])
class GameComponentTest : BasePostgresTest, BaseKafkaTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, PartyStartedMessage>
    @Autowired
    lateinit var getPartyByPlayer: GetPartyByPlayerFromDatabase

    private val ownerStartCell = point(x = 0, y = 0)

    @RepeatedTest(10)
    fun `all use cases as process`() {
        val party = createParty() // use case

        val ownerId = party.ownerId
        val startCellCount = party.cells.capturedCellCount()

        val dices = roll(ownerId) // use case

        captureCells(ownerId, dices, ownerStartCell) // use case

        val partyAfterCapture = getPartyByPlayer(ownerId)!!
        val expectedCapturedCellCount = startCellCount + dices.first * dices.second

        partyAfterCapture.cells.capturedCellCount() shouldBe expectedCapturedCellCount
    }

    private fun createParty(): Party {
        val partyStarted = partyStartedMessage()
        kafkaTemplate.send(PARTY_STARTED_TOPIC, partyStarted)
        return getParty(PlayerId(partyStarted.ownerId))
    }

    private fun getParty(ownerId: PlayerId): Party =
        runBlocking {
            eventually(
                eventuallyConfig {
                    duration = 10.seconds
                    initialDelay = 1.seconds
                }
            ) { getPartyByPlayer(ownerId)!! }
        }

    private fun roll(playerId: PlayerId) =
        mockMvc.post(API_V1_PLAYERS_DICES.with("playerId", playerId.toInt()))
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, RollResponse::class.java) }
            .dices

    private fun captureCells(
        playerId: PlayerId,
        dices: DicesResponse,
        startCell: Point
    ) {
        val x1 = startCell.x + 1
        val x2 = x1 + dices.first - 1
        val y1 = startCell.y
        val y2 = y1 + dices.second - 1
        val request = Request(
            first = Request.Point(x = x1, y = y1),
            second = Request.Point(x = x2, y = y2)
        )
        mockMvc.post(API_V1_PLAYERS_CELLS.with("playerId", playerId.toInt())) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect { status { isOk() } }
    }
}
