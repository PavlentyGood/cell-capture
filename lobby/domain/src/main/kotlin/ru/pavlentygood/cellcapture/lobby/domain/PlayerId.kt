package ru.pavlentygood.cellcapture.lobby.domain

data class PlayerId(
    private val value: Int
) {
    fun toInt() = value
}
