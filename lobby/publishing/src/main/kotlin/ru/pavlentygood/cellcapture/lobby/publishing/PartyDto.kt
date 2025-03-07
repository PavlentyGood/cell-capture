package ru.pavlentygood.cellcapture.lobby.publishing

import java.util.*

data class PartyDto(
    val id: UUID,
    val ownerId: Int,
    val players: List<PlayerDto>
)
