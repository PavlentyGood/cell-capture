package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        completed: Boolean,
        dices: Dices,
        cells: Array<Array<PlayerId>>,
        players: List<Player>,
        currentPlayerId: PlayerId,
        ownerId: PlayerId
    ): Either<DomainError, AbstractParty> {
        if (completed) {
            return CompletedParty(
                id = id,
                dices = dices,
                cells = cells,
                players = players,
                currentPlayerId = currentPlayerId,
                ownerId = ownerId
            ).right()
        }

        return PlayerList.from(
            ownerId = ownerId,
            players = players
        ).flatMap { playerList ->
            if (playerList.playerIds.contains(currentPlayerId)) {
                ActiveParty(
                    id = id,
                    dices = dices,
                    field = Field(
                        cells = cells
                    ),
                    ownerId = ownerId,
                    currentPlayerId = currentPlayerId,
                    players = playerList.players
                ).right()
            } else {
                IllegalCurrentPlayerId.left()
            }
        }
    }
}

data object IllegalCurrentPlayerId : DomainError
