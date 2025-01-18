package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.Either
import java.util.*
import kotlin.random.Random

fun <A> Either<Any, A>.get() =
    getOrNull()!!

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
    started: Boolean = false,
    playerLimit: Int = 2,
    owner: Player = player(),
    otherPlayers: List<Player> = listOf(),
    playerList: PlayerList = playerList(
        owner = owner,
        otherPlayers = otherPlayers
    )
) =
    Party(
        id = id,
        started = started,
        playerLimit = PlayerLimit(playerLimit),
        playerList = playerList,
        ownerId = owner.id
    )

fun player() =
    Player(
        id = playerId(),
        name = playerName()
    )

fun playerList(
    owner: Player,
    otherPlayers: List<Player>
) =
    PlayerList.restore(
        players = otherPlayers.toMutableList().apply { add(owner) }
    )
