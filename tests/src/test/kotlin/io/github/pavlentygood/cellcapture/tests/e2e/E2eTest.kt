package io.github.pavlentygood.cellcapture.tests.e2e

import io.github.pavlentygood.cellcapture.game.domain.Point
import io.github.pavlentygood.cellcapture.game.domain.point
import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import io.github.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import io.github.pavlentygood.cellcapture.kernel.domain.playerName
import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyResponse
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import io.github.pavlentygood.cellcapture.tests.e2e.client.*
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

typealias LobbyPartyResponse = io.github.pavlentygood.cellcapture.lobby.rest.api.PartyResponse
typealias GamePartyResponse = io.github.pavlentygood.cellcapture.game.rest.api.PartyResponse

@EnableFeignClients(basePackages = ["io.github.pavlentygood.cellcapture.tests.e2e.client"])
@SpringBootTest(
    classes = [
        FeignAutoConfiguration::class,
        JacksonAutoConfiguration::class,
        HttpMessageConvertersAutoConfiguration::class
    ]
)
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
    lateinit var getLobbyParty: GetLobbyPartyClient
    @Autowired
    lateinit var getGameParty: GetGamePartyClient

    private val ownerStartCell = point(x = 0, y = 0)
    private val startCellCount = 2

    init {
        startContainers()
    }

    @Test
    fun `play cell-capture project`() {
        val createdParty = createParty()
        val playerId = joinPlayer(createdParty.id)
        startParty(createdParty.ownerId)
        val dices = rollDices(createdParty.ownerId)
        captureCells(createdParty.ownerId, dices, ownerStartCell)
        val lobbyParty = getLobbyParty(createdParty.id)
        val gameParty = getGameParty(createdParty.ownerId)

        lobbyParty.id shouldBe gameParty.id
        lobbyParty.started shouldBe true
        lobbyParty.ownerId shouldBe gameParty.ownerId
        lobbyParty.players.map { it.id } shouldContainExactly gameParty.players.map { it.id }
        lobbyParty.players.map { it.name } shouldContainExactly gameParty.players.map { it.name }

        gameParty.completed shouldBe false
        gameParty.currentPlayerId shouldBe playerId
        gameParty.players.map { it.id } shouldContainExactly listOf(createdParty.ownerId, playerId)
        gameParty.cells.size shouldBe dices.first * dices.second + startCellCount
    }

    private fun createParty(): CreatePartyResponse =
        createParty(
            CreatePartyRequest(
                ownerName = playerName().toStringValue()
            )
        ).body!!

    private fun joinPlayer(partyId: UUID): Int =
        joinPlayer(
            partyId = partyId,
            request = JoinPlayerRequest(
                name = playerName().toStringValue()
            )
        ).body!!.id

    private fun startParty(ownerId: Int) =
        startParty.invoke(ownerId)

    private fun rollDices(ownerId: Int): RollDicesApi.DicesResponse =
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

    private fun captureCells(
        playerId: Int,
        dices: RollDicesApi.DicesResponse,
        startCell: Point
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

    private fun getLobbyParty(partyId: UUID): LobbyPartyResponse =
        getLobbyParty.invoke(partyId).body!!

    private fun getGameParty(playerId: Int): GamePartyResponse =
        getGameParty.invoke(playerId).body!!
}
