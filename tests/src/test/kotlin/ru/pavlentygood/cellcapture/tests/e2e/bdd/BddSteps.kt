package ru.pavlentygood.cellcapture.tests.e2e.bdd

import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import ru.pavlentygood.cellcapture.game.domain.point
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import ru.pavlentygood.cellcapture.tests.e2e.client.*
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class BddSteps(
    val createParty: CreatePartyClient,
    val joinPlayer: JoinPlayerClient,
    val startParty: StartPartyClient,
    val rollDices: RollDicesClient,
    val captureCells: CaptureCellsClient,
    val getLobbyParty: GetLobbyPartyClient,
    val getGameParty: GetGamePartyClient
) : En {

    private lateinit var ownerName: String
    private lateinit var playerName: String
    private lateinit var partyId: UUID
    private var ownerId: Int = 0
    private var playerId: Int = 0
    private var firstDice: Int = 0
    private var secondDice: Int = 0

    init {
        Given("имя владельца {word}") { name: String ->
            ownerName = name
        }

        And("имя игрока {word}") { name: String ->
            playerName = name
        }

        When("владелец создал партию") {
            val response = createParty(
                CreatePartyRequest(
                    ownerName = ownerName
                )
            ).body!!
            partyId = response.id
            ownerId = response.ownerId
        }

        And("игрок присоединился") {
            playerId = joinPlayer(
                partyId = partyId,
                request = JoinPlayerRequest(
                    name = playerName
                )
            ).body!!.id
        }

        And("владелец начал партию") {
            startParty(ownerId)
        }

        And("владелец сделал бросок") {
            runBlocking {
                eventually(
                    eventuallyConfig {
                        duration = 5.minutes
                        initialDelay = 5.seconds
                        interval = 5.seconds
                    }
                ) {
                    val dices = rollDices(ownerId).body!!.dices
                    firstDice = dices.first
                    secondDice = dices.second
                }
            }
        }

        And("владелец захватил клетки") {
            val startCell = point(x = 0, y = 0)
            val x1 = startCell.x + 1
            val x2 = x1 + firstDice - 1
            val y1 = startCell.y
            val y2 = y1 + secondDice - 1
            val request = CaptureCellsApi.Request(
                first = CaptureCellsApi.Request.Point(x = x1, y = y1),
                second = CaptureCellsApi.Request.Point(x = x2, y = y2)
            )
            captureCells.invoke(
                playerId = ownerId,
                request = request
            )
        }

        Then("следующие значения соответствуют друг-другу") { table: DataTable ->
            val lobbyParty = getLobbyParty(partyId).body!!
            val gameParty = getGameParty(ownerId).body!!

            val player = { id: Int ->
                gameParty.players
                    .single { it.id == id }
                    .let { "${it.id} ${it.name}" }
            }

            val map = mapOf(
                "истина" to true,
                "ложь" to false,
                "игрок Боб" to "$ownerId Боб",
                "игрок Алиса" to "$playerId Алиса",
                "партия в лобби" to lobbyParty.id,
                "партия в лобби начата" to lobbyParty.started,
                "владелец партии в лобби" to player(lobbyParty.ownerId),
                "игроки в лобби" to lobbyParty.players.map { "${it.id} ${it.name}" },
                "партия в игре" to gameParty.id,
                "партия в игре завершена" to gameParty.completed,
                "владелец партии в игре" to player(gameParty.ownerId),
                "текущий игрок" to player(gameParty.currentPlayerId),
                "игроки в игре" to gameParty.players.map { "${it.id} ${it.name}" },
                "кол-во захваченных клеток" to gameParty.cells.size,
                "площадь по выпавшим кубикам + стартовое кол-во клеток" to firstDice * secondDice + 2
            )

            table.asMap().forEach { (key, value) ->
                println("check $key and $value")
                map[key] shouldBe map[value]
            }
        }
    }
}
