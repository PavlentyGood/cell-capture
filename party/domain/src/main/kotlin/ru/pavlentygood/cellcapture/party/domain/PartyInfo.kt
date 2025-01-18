package ru.pavlentygood.cellcapture.party.domain

data class PartyInfo(
    val id: PartyId,
    val players: List<Player>
) {
    val ownerId: PlayerId = players.single { it.owner }.id
    val playerIds: List<PlayerId> = players.map { it.id }
}
