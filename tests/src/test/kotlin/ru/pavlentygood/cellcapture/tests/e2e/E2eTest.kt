package ru.pavlentygood.cellcapture.tests.e2e

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.kotest.common.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import ru.pavlentygood.cellcapture.game.rest.api.CellResponse
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyResponse
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import ru.pavlentygood.cellcapture.tests.e2e.client.*
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@EnableFeignClients
@SpringBootTest(classes = [
    CreatePartyClient::class,
    JoinPlayerClient::class,
    StartPartyClient::class,
    RollDicesClient::class,
    GetPartyClient::class,
    CaptureCellsClient::class,
    FeignAutoConfiguration::class,
    JacksonAutoConfiguration::class,
    HttpMessageConvertersAutoConfiguration::class
])
class E2eTest {

    @Autowired
    lateinit var createParty: CreatePartyClient
    @Autowired
    lateinit var joinPlayer: JoinPlayerClient
    @Autowired
    lateinit var startParty: StartPartyClient
    @Autowired
    lateinit var rollDices: RollDicesClient
    @Autowired
    lateinit var captureCells: CaptureCellsClient
    @Autowired
    lateinit var getParty: GetPartyClient

    init {
        Container.init()
    }

    @Test
    fun `play cell-capture project`() {
        val party = createParty()
        joinPlayer(party.id)
        startParty(party.ownerId)
        val dices = rollDices(party.ownerId)
        val startCell = findStartCell(party.ownerId)
        captureCells(party.ownerId, dices, startCell)
    }

    private fun createParty(): CreatePartyResponse =
        createParty(
            CreatePartyRequest(
                ownerName = playerName().toStringValue()
            )
        ).body!!

    private fun joinPlayer(partyId: UUID) =
        joinPlayer(
            partyId = partyId,
            request = JoinPlayerRequest(
                name = playerName().toStringValue()
            )
        ).body!!

    private fun startParty(ownerId: Int) =
        startParty.invoke(ownerId)

    private fun rollDices(ownerId: Int) =
        runBlocking {
            eventually(
                eventuallyConfig {
                    duration = 5.minutes
                    initialDelay = 5.seconds
                    interval = 5.seconds
                }
            ) {
                rollDices.invoke(ownerId).body!!.dices
            }
        }

    private fun findStartCell(playerId: Int): CellResponse {
        return getParty(playerId).body!!.cells
            .single { it.playerId == playerId }
    }

    private fun captureCells(
        playerId: Int,
        dices: RollDicesApi.DicesResponse,
        startCell: CellResponse
    ) {
        val x1 = startCell.x + 1
        val x2 = x1 + dices.first - 1
        val y1 = startCell.y
        val y2 = y1 + dices.second - 1
        val request = CaptureCellsApi.Request(
            first = CaptureCellsApi.Request.Point(x = x1, y = y1),
            second = CaptureCellsApi.Request.Point(x = x2, y = y2)
        )
        captureCells.invoke(
            playerId = playerId,
            request = request
        )
    }
}
