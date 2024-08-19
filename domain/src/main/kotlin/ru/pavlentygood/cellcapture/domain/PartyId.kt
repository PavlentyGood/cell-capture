package ru.pavlentygood.cellcapture.domain

import java.util.UUID

data class PartyId(
    private val value: UUID
) {
    fun toUUID() = value
}
