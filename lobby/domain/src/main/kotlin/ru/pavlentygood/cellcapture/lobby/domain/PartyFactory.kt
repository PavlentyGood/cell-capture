package ru.pavlentygood.cellcapture.lobby.domain

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
            playerList = PlayerList.create(firstPlayer = owner),
            ownerId = owner.id
        )
    }

    private fun createOwner(ownerName: PlayerName) =
        Player(
            id = generatePlayerId(),
            name = ownerName,
            owner = true
        )
}
