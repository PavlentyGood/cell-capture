package ru.pavlentygood.cellcapture.game.listening

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.player

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
