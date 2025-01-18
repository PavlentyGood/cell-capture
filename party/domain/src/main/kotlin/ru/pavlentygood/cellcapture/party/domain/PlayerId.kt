package ru.pavlentygood.cellcapture.party.domain

data class PlayerId(
    private val value: Int
) {
    fun toInt() = value
}
