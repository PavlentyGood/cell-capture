package io.github.pavlentygood.cellcapture.game.app.input.listening

import java.util.*

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
