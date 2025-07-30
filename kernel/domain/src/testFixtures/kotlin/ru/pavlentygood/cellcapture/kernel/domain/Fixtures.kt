package ru.pavlentygood.cellcapture.kernel.domain

import arrow.core.Either
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

private val counter = AtomicInteger(randomInt())

fun <A> Either<DomainError, A>.get() =
    onLeft { throw Exception("Either test error: $it") }
        .getOrNull()!!

fun randomInt(from: Int = 0, until: Int = 10000) =
    Random.nextInt(from, until)

fun partyId(value: UUID = UUID.randomUUID()) =
    PartyId(value)

fun playerId() =
    PlayerId(counter.incrementAndGet())

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
