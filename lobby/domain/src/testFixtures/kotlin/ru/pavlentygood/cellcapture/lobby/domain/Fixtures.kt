package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.*

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
