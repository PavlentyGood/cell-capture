package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right

const val MIN_PLAYER_COUNT = 2

data class PlayerLimit internal constructor(
    val value: Int
) {
    fun isExceeded(playerCount: Int) =
        playerCount >= value

    companion object {
        fun from(limit: Int) =
            if (limit < MIN_PLAYER_COUNT) {
                InvalidPlayerLimit.left()
            } else {
                PlayerLimit(limit).right()
            }
    }
}

object InvalidPlayerLimit
