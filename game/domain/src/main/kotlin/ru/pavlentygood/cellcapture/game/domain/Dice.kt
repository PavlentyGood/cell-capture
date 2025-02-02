package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError
import kotlin.random.Random

data class Dice internal constructor(
    val value: Int
) {
    companion object {
        const val MIN = 1
        const val MAX = 6

        fun roll() =
            Dice(
                value = Random.nextInt(from = MIN, until = MAX + 1)
            )

        fun from(value: Int) =
            if (value in MIN..MAX) {
                Dice(value).right()
            } else {
                InvalidValue.left()
            }
    }

    data object InvalidValue : DomainError
}
