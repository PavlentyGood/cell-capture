package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import java.util.*
import kotlin.random.Random

fun <A> Either<Any, A>.get() =
    onLeft { throw Exception("Either test error: $it") }
        .getOrNull()!!

fun randomInt(from: Int = 0, until: Int = 1000) =
    Random.nextInt(from, until)

fun partyId() =
    PartyId(UUID.randomUUID())

fun playerId(until: Int = 1000) =
    PlayerId(randomInt(until = until))

fun playerName() =
    PlayerName.from(
        name = "Bob ${randomInt()}"
    ).get()

fun party(
    id: PartyId = partyId(),
    completed: Boolean = false,
    owner: Player = player(),
    otherPlayers: List<Player> = listOf(player()),
    dicePair: DicePair? = dicePair(),
    field: Field = field(),
    playerQueue: PlayerQueue = playerQueue(
        owner = owner,
        otherPlayers = otherPlayers
    )
) =
    Party(
        id = id,
        completed = completed,
        dicePair = dicePair,
        field = field,
        playerQueue = playerQueue,
        ownerId = owner.id
    )

fun player(id: PlayerId = playerId()) =
    Player(
        id = id,
        name = playerName()
    )

fun area(distanceToEdges: Int = 0) =
    Area.from(
        first = point(distanceToEdges),
        second = point(distanceToEdges)
    )

fun point(x: Int, y: Int) =
    Point.from(
        x = x,
        y = y
    ).get()

fun point(distanceToEdges: Int = 0) =
    point(
        x = randomInt(from = distanceToEdges, until = Field.WIDTH - distanceToEdges),
        y = randomInt(from = distanceToEdges, until = Field.HEIGHT - distanceToEdges)
    )

fun dicePair() =
    DicePair(
        first = dice(),
        second = dice()
    )

fun dice(value: Int = randomInt(from = 1, until = 7)) =
    Dice.from(
        value = value
    ).get()

fun field(cells: Array<Array<PlayerId>> = cells()) =
    Field(
        cells = cells
    )

fun cells() =
    Array(Field.HEIGHT) {
        Array(Field.WIDTH) { Field.nonePlayerId }
    }

fun Array<Array<PlayerId>>.capturedCellCount() =
    sumOf { line ->
        line.count { id -> id != Field.nonePlayerId }
    }

fun playerQueue(
    owner: Player,
    otherPlayers: List<Player>
) =
    PlayerQueue.restore(
        playerList = playerList(
            ownerId = owner.id,
            players = otherPlayers.toMutableList().apply { add(owner) }
        ),
        currentPlayerId = owner.id
    ).get()

fun partyInfo() =
    PartyInfo(
        partyId = partyId(),
        playerList = playerList()
    )

fun playerList(
    ownerId: PlayerId = playerId(until = 1000000),
    players: List<Player> = listOf(player(ownerId), player(playerId(until = 1000000)))
) =
    PlayerList.from(
        ownerId = ownerId,
        players = players
    ).get()
