package ru.pavlentygood.cellcapture.domain

import java.util.*

const val DEFAULT_PLAYER_LIMIT = 4

class PartyFactory {
    fun create() =
        Party(
            id = PartyId(UUID.randomUUID()),
            playerLimit = PlayerLimit(DEFAULT_PLAYER_LIMIT),
            players = mutableListOf(),
            status = Party.Status.NEW
        )
}
