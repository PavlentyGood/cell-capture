package ru.pavlentygood.cellcapture.game.persistence

import java.util.UUID

data class PartyDto(
    val id: UUID,
    val completed: Boolean,
    val firstDice: Int?,
    val secondDice: Int?,
    val ownerId: Int,
    val currentPlayerId: Int
)
