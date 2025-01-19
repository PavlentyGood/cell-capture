package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import kotlin.random.Random

class Field(
   private val cells: Array<Array<PlayerId>>
) {
    fun getCells() = cells.copyOf()

    fun appointStartCells(playerIds: List<PlayerId>) {
        return generateStartCells(playerIds)
            .forEach {
                cells[it.y][it.x] = it.playerId
            }
    }

    private tailrec fun generateStartCells(
        playerIds: List<PlayerId>,
        cells: List<Cell> = listOf()
    ): List<Cell> =
        if (playerIds.isEmpty()) {
            cells
        } else {
            val cell = generateSequence {
                Cell(
                    playerId = playerIds.first(),
                    x = Random.nextInt(WIDTH),
                    y = Random.nextInt(HEIGHT)
                )
            }.first { cell ->
                cells.none { it.x == cell.x && it.y == cell.y }
            }

            generateStartCells(playerIds.drop(1), cells.plus(cell))
        }

    fun capture(playerId: PlayerId, area: Area) =
        if (area.isAnyCellCaptured() || !area.isTouchOwnCell(playerId)) {
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

    private fun Area.isAnyCellCaptured() =
        rangeByY().any { y ->
            rangeByX().any { x ->
                cells[y][x] != nonePlayerId
            }
        }

    private fun Area.isTouchOwnCell(playerId: PlayerId): Boolean {
        fun byRanges(xRange: IntRange, yRange: IntRange) =
            yRange.any { y ->
                xRange.any { x ->
                    cells[y][x] == playerId
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
