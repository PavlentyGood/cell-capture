package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.game.domain.Field.Companion.HEIGHT
import ru.pavlentygood.cellcapture.game.domain.Field.Companion.WIDTH
import ru.pavlentygood.cellcapture.game.domain.Field.Companion.nonePlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class Field internal constructor(
    private val cells: Array<Array<Cell>>
) {
    fun getCells() = cells.copyOf()

    fun capture(playerId: PlayerId, area: Area) =
        if (area.isAnyCellCaptured() || !area.isTouchOwnCell(playerId)) {
            InaccessibleArea.left()
        } else {
            area.capture(playerId).right()
        }

    private fun Area.capture(playerId: PlayerId) =
        rangeByY().forEach { y ->
            rangeByX().forEach { x ->
                cells[y][x] = Cell(
                    playerId = playerId,
                    x = x,
                    y = y
                )
            }
        }

    private fun Area.isAnyCellCaptured() =
        rangeByY().any { y ->
            rangeByX().any { x ->
                cells[y][x].playerId != nonePlayerId
            }
        }

    private fun Area.isTouchOwnCell(playerId: PlayerId): Boolean {
        fun byRanges(xRange: IntRange, yRange: IntRange) =
            yRange.any { y ->
                xRange.any { x ->
                    cells[y][x].playerId == playerId
                }
            }

        fun byXRange(y: Int) = byRanges(rangeByX(), rangeBySingle(y, HEIGHT))
        fun byYRange(x: Int) = byRanges(rangeBySingle(x, WIDTH), rangeByY())

        fun byTopSide() = byXRange(from.y - 1)
        fun byBottomSide() = byXRange(to.y + 1)
        fun byLeftSide() = byYRange(from.x - 1)
        fun byRightSide() = byYRange(to.x + 1)

        return byTopSide() || byBottomSide() || byLeftSide() || byRightSide()
    }

    private fun Area.rangeByX() =
        from.x..to.x

    private fun Area.rangeByY() =
        from.y..to.y

    private fun rangeBySingle(value: Int, maxUntil: Int) =
        if (value in Point.MIN until maxUntil) {
            value..value
        } else {
            IntRange.EMPTY
        }

    companion object {
        const val WIDTH = 64
        const val HEIGHT = 48

        val nonePlayerId = PlayerId(0)
    }
}

fun createCells(): Array<Array<Cell>> =
    Array(HEIGHT) { y ->
        Array(WIDTH) { x ->
            Cell(nonePlayerId, x, y)
        }
    }
