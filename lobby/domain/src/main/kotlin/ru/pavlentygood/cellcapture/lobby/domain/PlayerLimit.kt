package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.MAX_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.MIN_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

data class PlayerLimit internal constructor(
    val value: Int
) {
    fun isReached(playerCount: Int) =
        playerCount >= value

    fun isExceeded(playerCount: Int) =
        playerCount > value

    companion object {
        fun from(limit: Int) =
            when {
                limit !in MIN_PLAYER_COUNT..MAX_PLAYER_COUNT -> IllegalPlayerLimit.left()
                else -> PlayerLimit(limit).right()
            }
    }
}

object IllegalPlayerLimit : DomainError
