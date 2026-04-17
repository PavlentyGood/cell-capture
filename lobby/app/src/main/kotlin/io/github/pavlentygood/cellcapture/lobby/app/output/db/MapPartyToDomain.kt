package io.github.pavlentygood.cellcapture.lobby.app.output.db

import arrow.core.getOrElse
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PartyDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PlayerDto
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.domain.Player
import io.github.pavlentygood.cellcapture.lobby.domain.PlayerLimit
import io.github.pavlentygood.cellcapture.lobby.domain.restoreParty

fun mapPartyToDomain(dto: PartyDto): Party =
    restoreParty(
        id = PartyId(dto.partyId),
        version = Version.from(dto.version).getOrElse {
            error("Illegal version: $it. version: ${dto.version}")
        },
        started = dto.started,
        ownerId = PlayerId(dto.ownerId),
        playerLimit = PlayerLimit.from(dto.playerLimit)
            .getOrElse {
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
