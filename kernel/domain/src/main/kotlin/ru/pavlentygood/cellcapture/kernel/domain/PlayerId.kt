package ru.pavlentygood.cellcapture.kernel.domain

data class PlayerId(
    private val value: Int
) {
    fun toInt() = value
}
