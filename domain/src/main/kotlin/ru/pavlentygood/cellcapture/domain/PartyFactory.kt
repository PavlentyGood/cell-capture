package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import java.util.*

class Party(
    val id: PartyId
) {
    fun joinPlayer(name: PlayerName): Either<PlayerCountLimitExceeded, PlayerId> {
        TODO("Not yet implemented")
    }
}

object PlayerCountLimitExceeded

class PartyFactory {
    fun create() =
        Party(PartyId(UUID.randomUUID()))
}
