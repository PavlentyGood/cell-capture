package ru.pavlentygood.cellcapture.party.domain

import arrow.core.Either

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        playerLimit: PlayerLimit,
        status: Party.Status,
        dicePair: DicePair?,
        cells: Array<Array<PlayerId>>,
        players: List<Player>,
        currentPlayerId: PlayerId,
        ownerId: PlayerId
    ): Either<PlayerQueue.InvalidPlayerQueue, Party> =
        PlayerQueue.restore(
            players = players,
            currentPlayerId = currentPlayerId
        ).map { playerQueue ->
            Party(
                id = id,
                playerLimit = playerLimit,
                status = status,
                dicePair = dicePair,
                field = Field(
                    cells = cells
                ),
                playerQueue = playerQueue,
                ownerId = ownerId
            )
        }
}
