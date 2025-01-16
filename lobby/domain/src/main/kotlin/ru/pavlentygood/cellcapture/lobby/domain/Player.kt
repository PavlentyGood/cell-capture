package ru.pavlentygood.cellcapture.lobby.domain

data class Player(
    val id: PlayerId,
    val name: PlayerName,
    val owner: Boolean
)
