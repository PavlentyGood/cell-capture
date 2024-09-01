package ru.pavlentygood.cellcapture.domain

data class PlayerId(
    private val value: Int
) {
    fun toInt() = value
}
