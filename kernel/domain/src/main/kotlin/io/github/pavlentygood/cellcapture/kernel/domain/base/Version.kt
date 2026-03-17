package io.github.pavlentygood.cellcapture.kernel.domain.base

import arrow.core.Either
import arrow.core.left
import arrow.core.right

data class Version internal constructor(
    val value: Long
) {
    fun next() = Version(value + 1)
    fun previous() = Version(value - 1)

    companion object {

        fun new() = Version(0)

        fun from(value: Long): Either<IllegalVersion, Version> =
            if (value >= 1) {
                Version(value).right()
            } else {
                IllegalVersion.left()
            }
    }
}

data object IllegalVersion : DomainError
