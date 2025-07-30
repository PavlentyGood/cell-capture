package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId

data class PartyInfo(
    val partyId: PartyId,
    val playerList: PlayerList
) {
    val ownerId by playerList::ownerId
    val players by playerList::players
}
