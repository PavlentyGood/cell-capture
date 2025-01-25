package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import java.util.*

const val DEFAULT_PLAYER_LIMIT = 4

class PartyFactory(
    private val generatePlayerId: GeneratePlayerId
) {
    fun create(ownerName: PlayerName): Party {
        val owner = createOwner(ownerName)
        return Party(
            id = PartyId(UUID.randomUUID()),
            started = false,
            playerLimit = PlayerLimit(DEFAULT_PLAYER_LIMIT),
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
