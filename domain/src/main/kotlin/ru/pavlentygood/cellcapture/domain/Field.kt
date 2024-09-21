package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right
import kotlin.math.max
import kotlin.math.min

class Field(
   private val cells: Array<Array<PlayerId>>
) {
    fun getCells() = cells.copyOf()

    fun capture(playerId: PlayerId, area: Area) =
        if (area.isInaccessible()) {
            Party.InaccessibleArea.left()
        } else {
            area.capture(playerId).right()
        }

    private fun Area.capture(playerId: PlayerId) =
        rangeByY().forEach { y ->
            rangeByX().forEach { x ->
                cells[y][x] = playerId
            }
        }

    private fun Area.isInaccessible() =
        rangeByY().any { y ->
            rangeByX().any { x ->
                cells[y][x] != nonePlayerId
            }
        }

    private fun Area.rangeByX() =
        rangeBy { it.x }

    private fun Area.rangeByY() =
        rangeBy { it.y }

    private fun Area.rangeBy(coord: (Point) -> Int) =
        (min(coord(from), coord(to)) .. max(coord(from), coord(to)))

    companion object {
        const val WIDTH = 64
        const val HEIGHT = 48

        val nonePlayerId = PlayerId(0)
    }
}
