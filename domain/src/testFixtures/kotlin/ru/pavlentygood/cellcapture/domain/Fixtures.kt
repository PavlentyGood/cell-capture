package ru.pavlentygood.cellcapture.domain

import java.util.*
import kotlin.random.Random

fun randomInt() = Random.nextInt(1, 1000)

fun partyId() = PartyId(UUID.randomUUID())

fun playerId() = PlayerId(randomInt())

fun playerName() = PlayerName("Bob ${randomInt()}")
