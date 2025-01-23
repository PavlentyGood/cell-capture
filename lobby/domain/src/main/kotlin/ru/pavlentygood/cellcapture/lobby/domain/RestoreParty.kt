package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        started: Boolean,
        playerLimit: PlayerLimit,
        players: List<Player>,
        ownerId: PlayerId
    ): Party =
        Party(
            id = id,
            started = started,
            playerLimit = playerLimit,
            playerList = PlayerList.restore(
                players = players
            ),
            ownerId = ownerId
        )
}
