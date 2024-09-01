package ru.pavlentygood.cellcapture.domain

import arrow.core.left
import arrow.core.right

data class PlayerName internal constructor(
    private val value: String
) {
    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 20

        fun from(name: String) =
            name.trim().let {
                if (it.length in MIN_LENGTH..MAX_LENGTH) {
                    PlayerName(it).right()
                } else {
                    InvalidPlayerName.left()
                }
            }
    }
}

object InvalidPlayerName
