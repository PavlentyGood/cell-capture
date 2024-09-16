package ru.pavlentygood.cellcapture.domain

import java.util.*
import kotlin.random.Random

fun randomInt(from: Int = 0) = Random.nextInt(from, 1000)

fun partyId() = PartyId(UUID.randomUUID())

fun playerId() = PlayerId(randomInt())

fun playerName() = PlayerName("Bob ${randomInt()}")

fun party(
    id: PartyId = partyId(),
    playerLimit: Int = 2,
    players: List<Player> = listOf(player()),
    status: Party.Status = Party.Status.NEW
): Party {
    val owner = player(owner = true)
    return Party(
        id = id,
        playerLimit = PlayerLimit(playerLimit),
        players = mutableListOf(owner).apply { addAll(players) },
        status = status
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
        from = cell(),
        to = cell()
    )

fun cell() =
    Cell(
        x = randomInt(),
        y = randomInt()
    )
