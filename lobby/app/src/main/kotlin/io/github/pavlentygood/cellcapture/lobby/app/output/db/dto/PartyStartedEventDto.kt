package io.github.pavlentygood.cellcapture.lobby.app.output.db.dto

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
