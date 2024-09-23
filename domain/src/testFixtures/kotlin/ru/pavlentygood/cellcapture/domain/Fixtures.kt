package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import java.util.*
import kotlin.random.Random

fun <A> Either<Any, A>.get() =
    getOrNull()!!

fun randomInt(from: Int = 0, until: Int = 1000) =
    Random.nextInt(from, until)

fun partyId() =
    PartyId(UUID.randomUUID())

fun playerId() =
    PlayerId(randomInt())

fun playerName() =
    PlayerName.from(
        name = "Bob ${randomInt()}"
    ).get()

fun party(
    id: PartyId = partyId(),
    playerLimit: Int = 2,
    players: List<Player> = listOf(player()),
    status: Party.Status = Party.Status.NEW,
    dicePair: DicePair = dicePair(),
    field: Field = field()
): Party {
    val owner = player(owner = true)
    return Party(
        id = id,
        playerLimit = PlayerLimit(playerLimit),
        players = mutableListOf(owner).apply { addAll(players) },
        status = status,
        currentPlayerId = null,
        dicePair = dicePair,
        field = field
    )
}

fun player(owner: Boolean = false) =
    Player(
        id = playerId(),
        name = playerName(),
        owner = owner
    )

fun area() =
    Area(
        from = point(),
        to = point()
    )

fun point() =
    Point.from(
        x = randomInt(until = Field.WIDTH),
        y = randomInt(until = Field.HEIGHT)
    ).get()

fun dicePair() =
    DicePair(
        first = dice(),
        second = dice()
    )

fun dice() =
    Dice.from(
        value = randomInt(from = 1, until = 7)
    ).get()

fun field() =
    Field(
        cells = cells()
    )

fun cells() =
    Array(Field.HEIGHT) {
        Array(Field.WIDTH) { Field.nonePlayerId }
    }

fun Array<Array<PlayerId>>.capturedCellCount() =
    sumOf { line ->
        line.count { id -> id != Field.nonePlayerId }
    }
