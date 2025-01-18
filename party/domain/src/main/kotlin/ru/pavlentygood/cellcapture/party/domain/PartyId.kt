package ru.pavlentygood.cellcapture.party.domain

import java.util.UUID

data class PartyId(
    private val value: UUID
) {
    fun toUUID() = value
}
