package ru.pavlentygood.cellcapture.domain

import java.util.*

class Party(
    val id: PartyId
)

class PartyFactory {
    fun create() =
        Party(PartyId(UUID.randomUUID()))
}
