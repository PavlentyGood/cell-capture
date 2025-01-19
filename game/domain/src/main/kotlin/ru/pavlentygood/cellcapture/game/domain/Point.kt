package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right

data class Point internal constructor(
    val x: Int,
    val y: Int
) {
    companion object {
        const val MIN = 0

        fun from(x: Int, y: Int) =
            if (x in MIN until Field.WIDTH && y in MIN until Field.HEIGHT) {
                Point(x, y).right()
            } else {
                InvalidValue.left()
            }
    }

    data object InvalidValue
}
