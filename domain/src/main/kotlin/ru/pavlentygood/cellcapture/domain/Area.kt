package ru.pavlentygood.cellcapture.domain

import kotlin.math.abs

data class Area(
    val from: Point,
    val to: Point
) {
    fun xDistance() =
        distance { it.x }

    fun yDistance() =
        distance { it.y }

    private fun distance(coord: (Point) -> Int) =
        abs(coord(from) - coord(to))
}
