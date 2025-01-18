package ru.pavlentygood.cellcapture.party.domain

import java.util.*

class PartyFactory {
    fun create(partyInfo: PartyInfo): Party {
        return Party(
            id = PartyId(UUID.randomUUID()),
            completed = false,
            dicePair = null,
            field = createField(partyInfo),
            playerQueue = PlayerQueue.create(partyInfo),
            ownerId = partyInfo.ownerId
        )
    }

    private fun createField(partyInfo: PartyInfo) =
        Field(
            cells = createCells()
        ).also {
            it.appointStartCells(partyInfo.playerIds)
        }

    private fun createCells() =
        Array(Field.HEIGHT) {
            Array(Field.WIDTH) { Field.nonePlayerId }
        }
}
