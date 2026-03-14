package ru.pavlentygood.cellcapture.game.app

import ru.pavlentygood.cellcapture.game.app.listening.PartyStartedMessage
import ru.pavlentygood.cellcapture.game.domain.Player
import ru.pavlentygood.cellcapture.game.domain.player
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.partyId

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
