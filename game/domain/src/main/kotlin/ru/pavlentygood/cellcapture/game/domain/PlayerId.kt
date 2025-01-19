package ru.pavlentygood.cellcapture.game.domain

data class PlayerId(
    private val value: Int
) {
    fun toInt() = value
}
