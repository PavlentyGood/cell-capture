package ru.pavlentygood.cellcapture.lobby.persistence.dto

data class PartyStartedEventDto(
    val partyId: String,
    val ownerId: Int,
    val players: List<Player>
) {
    data class Player(
        val id: Int,
        val name: String
    )
}
