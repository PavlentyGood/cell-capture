package ru.pavlentygood.cellcapture.domain

import java.util.*
import kotlin.random.Random

fun randomInt(from: Int = 0) = Random.nextInt(from, 1000)

fun partyId() = PartyId(UUID.randomUUID())

fun playerId() = PlayerId(randomInt())

fun playerName() = PlayerName("Bob ${randomInt()}")

fun party(id: PartyId = partyId(), playerLimit: Int = 1) =
    Party(
        id = id,
        playerLimit = PlayerLimit(playerLimit),
        players = mutableListOf()
    )
