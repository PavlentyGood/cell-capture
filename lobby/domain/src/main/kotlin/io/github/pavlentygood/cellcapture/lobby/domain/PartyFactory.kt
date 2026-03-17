package io.github.pavlentygood.cellcapture.lobby.domain

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version
import java.util.*

const val DEFAULT_PLAYER_LIMIT = 4

class PartyFactory(
    private val generatePlayerId: GeneratePlayerId
) {
    fun create(ownerName: PlayerName): Party {
        val partyId = PartyId(UUID.randomUUID())
        val owner = createOwner(ownerName)
        return Party(
            events = listOf(PartyCreatedEvent(partyId)),
            id = partyId,
            version = Version.new(),
            started = false,
            playerLimit = PlayerLimit(
                DEFAULT_PLAYER_LIMIT
            ),
            players = listOf(owner),
            ownerId = owner.id
        )
    }

    private fun createOwner(ownerName: PlayerName) =
        Player(
            id = generatePlayerId(),
            name = ownerName
        )
}
