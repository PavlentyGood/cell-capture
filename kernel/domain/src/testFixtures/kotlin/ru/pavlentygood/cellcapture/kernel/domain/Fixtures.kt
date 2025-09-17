package ru.pavlentygood.cellcapture.kernel.domain

import arrow.core.Either
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError
import ru.pavlentygood.cellcapture.kernel.domain.base.Version
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

private val counter = AtomicInteger(randomInt())

fun <A> Either<DomainError, A>.get() =
    onLeft { throw Exception("Either test error: $it") }
        .getOrNull()!!

fun randomInt(from: Int = 0, until: Int = 10000) =
    Random.nextInt(from, until)

fun version(
    value: Long = randomInt().toLong()
) =
    Version.from(value).get()

fun partyId(value: UUID = UUID.randomUUID()) =
    PartyId(value)

fun playerId(value: Int = counter.incrementAndGet()) =
    PlayerId(value)

fun playerName(value: String = "Bob ${randomInt()}") =
    PlayerName.from(value).get()
