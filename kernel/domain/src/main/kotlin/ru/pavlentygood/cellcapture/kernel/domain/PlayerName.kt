package ru.pavlentygood.cellcapture.kernel.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

data class PlayerName internal constructor(
    private val value: String
) {
    fun toStringValue() = value

    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 20

        fun from(name: String): Either<IllegalPlayerNameLength, PlayerName> =
            name.trim().let {
                when {
                    it.length in MIN_LENGTH..MAX_LENGTH -> PlayerName(it).right()
                    else -> IllegalPlayerNameLength.left()
                }
            }
    }
}

object IllegalPlayerNameLength : DomainError
