package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right

data class PlayerLimit internal constructor(
    val value: Int
) {
    fun isExceeded(playerCount: Int) =
        playerCount >= value

    companion object {
        private const val MIN = 2

        fun from(limit: Int) =
            if (limit < MIN) {
                InvalidPlayerLimit.left()
            } else {
                PlayerLimit(limit).right()
            }
    }
}

object InvalidPlayerLimit
