package ru.pavlentygood.cellcapture.lobby.persistence.dto

import java.util.*

data class PartyStartedEventDto(
    val partyId: UUID,
    val ownerId: Int,
    val players: List<Player>
) {
    data class Player(
        val id: Int,
        val name: String
    )
}
