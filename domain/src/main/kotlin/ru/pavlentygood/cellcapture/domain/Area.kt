package ru.pavlentygood.cellcapture.domain

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Area internal constructor(
    val from: Point,
    val to: Point
) {
    fun xDistance() =
        distance { it.x }

    fun yDistance() =
        distance { it.y }

    private fun distance(coord: (Point) -> Int) =
        abs(coord(from) - coord(to))

    companion object {

        fun from(first: Point, second: Point) =
            Area(
                from = Point(
                    x = min(first.x, second.x),
                    y = min(first.y, second.y)
                ),
                to = Point(
                    x = max(first.x, second.x),
                    y = max(first.y, second.y)
                )
            )
    }
}
