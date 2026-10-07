package io.github.pavlentygood.cellcapture.ai.domain

import arrow.core.left
import arrow.core.right
import io.github.pavlentygood.cellcapture.kernel.domain.base.DomainError

const val MAX_FOREST_NAME_LENGTH = 50

data class ForestName private constructor(
    val value: String
) {
    fun toText() = value

    companion object {

        fun of(value: String) =
            when {
                value.isBlank() -> ForestNameError.Blank.left()
                value.length > MAX_FOREST_NAME_LENGTH -> ForestNameError.TooLong.left()
                else -> ForestName(value).right()
            }
    }
}

sealed interface ForestNameError : DomainError {
    data object Blank : ForestNameError
    data object TooLong : ForestNameError
}
