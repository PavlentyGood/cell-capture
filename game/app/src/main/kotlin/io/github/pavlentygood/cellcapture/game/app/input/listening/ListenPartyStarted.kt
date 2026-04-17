package io.github.pavlentygood.cellcapture.game.app.input.listening

import arrow.core.getOrElse
import io.github.pavlentygood.cellcapture.game.domain.PartyInfo
import io.github.pavlentygood.cellcapture.game.domain.Player
import io.github.pavlentygood.cellcapture.game.domain.PlayerList
import io.github.pavlentygood.cellcapture.game.app.usecase.CreatePartyUseCase
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName

class ListenPartyStarted(
    private val createParty: CreatePartyUseCase
) {
    operator fun invoke(message: PartyStartedMessage) {
        val partyInfo: PartyInfo = message.toDomain()
        createParty(partyInfo)
    }
}

fun PartyStartedMessage.toDomain() =
    PartyInfo(
        partyId = PartyId(partyId),
        playerList = PlayerList.from(
            ownerId = PlayerId(ownerId),
            players = players.map { it.toDomain() }
        ).getOrElse {
            error("Illegal player list: $it")
        }
    )

fun PartyStartedMessage.Player.toDomain() =
    Player(
        id = PlayerId(id),
        name = PlayerName.from(name).getOrElse {
            error("Illegal player name ($name): $it")
        }
    )
