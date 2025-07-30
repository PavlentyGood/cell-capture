package ru.pavlentygood.cellcapture.lobby.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PlayerLimit
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PlayerDto

class MapPartyToDomain(
    private val restoreParty: RestoreParty
) {
    operator fun invoke(dto: PartyDto): Party =
        restoreParty(
            id = PartyId(dto.id),
            started = dto.started,
            ownerId = PlayerId(dto.ownerId),
            playerLimit = PlayerLimit.from(dto.playerLimit).getOrElse {
                error("Illegal player limit: ${dto.playerLimit}")
            },
            players = dto.players.map { toDomain(it) }
        ).getOrElse {
            error("Restore party error: $it")
        }

    private fun toDomain(dto: PlayerDto): Player =
        Player(
            id = PlayerId(dto.id),
            name = PlayerName.from(dto.name).getOrElse {
                error("Illegal player name length: ${dto.name}")
            },
        )
}
