package ru.pavlentygood.cellcapture.game.listening

import java.util.UUID

data class PartyStartedMessage(
    val partyId: UUID,
    val ownerId: Int,
    val players: List<Player>
) {
    data class Player(
        val id: Int,
        val name: String
    )
}
