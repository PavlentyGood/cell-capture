package io.github.pavlentygood.cellcapture.kernel.domain

import java.util.*

data class PartyId(
    private val value: UUID
) {
    fun toUUID() = value
}
