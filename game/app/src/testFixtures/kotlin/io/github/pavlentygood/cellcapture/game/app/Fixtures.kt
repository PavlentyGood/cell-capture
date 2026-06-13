package io.github.pavlentygood.cellcapture.game.app

import io.github.pavlentygood.cellcapture.game.app.input.listening.PartyStartedMessage
import io.github.pavlentygood.cellcapture.game.domain.Player
import io.github.pavlentygood.cellcapture.game.domain.player
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.partyId

fun partyStartedMessage(
    partyId: PartyId = partyId(),
    owner: Player = player(),
    player: Player = player()
) =
    PartyStartedMessage(
        partyId = partyId.toUUID(),
        ownerId = owner.id.toInt(),
        players = listOf(
            PartyStartedMessage.Player(
                id = owner.id.toInt(),
                name = owner.name.toStringValue()
            ),
            PartyStartedMessage.Player(
                id = player.id.toInt(),
                name = player.name.toStringValue()
            )
        )
    )
