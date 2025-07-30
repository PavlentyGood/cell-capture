package ru.pavlentygood.cellcapture.kernel.domain

import java.util.UUID

data class PartyId(
    private val value: UUID
) {
    fun toUUID() = value
}
