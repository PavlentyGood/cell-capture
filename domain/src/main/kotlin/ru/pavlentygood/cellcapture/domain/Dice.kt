package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right

data class Dice internal constructor(
    val value: Int
) {
    companion object {
        const val MIN = 1
        const val MAX = 6

        fun from(value: Int) =
            if (value in MIN..MAX) {
                Dice(value).right()
            } else {
                InvalidValue.left()
            }

        fun nonRolled() =
            Dice(1)
    }

    data object InvalidValue
}
