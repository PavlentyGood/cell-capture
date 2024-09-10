package ru.pavlentygood.cellcapture.domain

import java.util.*
import kotlin.random.Random

fun randomInt(from: Int = 0) = Random.nextInt(from, 1000)

fun partyId() = PartyId(UUID.randomUUID())

fun playerId() = PlayerId(randomInt())

fun playerName() = PlayerName("Bob ${randomInt()}")

fun party(
    id: PartyId = partyId(),
    playerLimit: Int = 1,
    players: List<Player> = listOf(),
    status: Party.Status = Party.Status.NEW
) =
    Party(
        id = id,
        playerLimit = PlayerLimit(playerLimit),
        players = players.toMutableList(),
        status = status
    )

fun partyAndOwner(
    status: Party.Status = Party.Status.NEW
): Pair<Party, Player> {
    val owner = player(owner = true)
    val party = party(
        players = listOf(owner, player()),
        status = status
    )
    return Pair(party, owner)
}

fun player(owner: Boolean = false) =
    Player(
        id = playerId(),
        name = playerName(),
        owner = owner
    )
