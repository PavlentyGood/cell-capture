package ru.pavlentygood.cellcapture.game.domain

data class PartyInfo(
    val partyId: PartyId,
    val playerList: PlayerList
) {
    val ownerId by playerList::ownerId
    val players by playerList::players
}
