package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.*

fun party(
    id: PartyId = partyId(),
    started: Boolean = false,
    playerLimit: Int = 2,
    owner: Player = player(),
    otherPlayers: List<Player> = listOf()
) =
    Party(
        id = id,
        started = started,
        playerLimit = playerLimit(playerLimit),
        players = listOf(owner).plus(otherPlayers),
        ownerId = owner.id
    )

fun player() =
    Player(
        id = playerId(),
        name = playerName()
    )

fun playerLimit(value: Int = 2) =
    PlayerLimit.from(value).get()
