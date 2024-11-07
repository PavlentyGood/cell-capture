package ru.pavlentygood.cellcapture.domain

data class Player(
    val id: PlayerId,
    val name: PlayerName,
    val owner: Boolean
)
