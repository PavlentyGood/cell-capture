package ru.pavlentygood.cellcapture.kernel.domain

import arrow.core.Either
import java.util.*
import kotlin.random.Random

fun <A> Either<Any, A>.get() =
    onLeft { throw Exception("Either test error: $it") }
        .getOrNull()!!

fun randomInt(from: Int = 0, until: Int = 10000) =
    Random.nextInt(from, until)

fun partyId(value: UUID = UUID.randomUUID()) =
    PartyId(value)

fun playerId(until: Int = 10000) =
    PlayerId(randomInt(until = until))

fun playerName(value: String = "Bob ${randomInt()}") =
    PlayerName.from(value).get()

fun player(
    playerId: PlayerId = playerId(),
    playerName: PlayerName = playerName()
) =
    Player(
        id = playerId,
        name = playerName
    )
