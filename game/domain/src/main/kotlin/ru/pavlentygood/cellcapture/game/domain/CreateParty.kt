package ru.pavlentygood.cellcapture.game.domain

class CreateParty {

    operator fun invoke(partyInfo: PartyInfo): Party {
        return Party(
            id = partyInfo.partyId,
            completed = false,
            dicePair = null,
            field = createField(partyInfo.playerList),
            playerQueue = PlayerQueue.create(partyInfo.playerList),
            ownerId = partyInfo.ownerId
        )
    }

    private fun createField(playerList: PlayerList) =
        Field(
            cells = createCells()
        ).also {
            it.appointStartCells(playerList.playerIds)
        }

    private fun createCells() =
        Array(Field.HEIGHT) {
            Array(Field.WIDTH) { Field.nonePlayerId }
        }
}
