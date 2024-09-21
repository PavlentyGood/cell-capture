package ru.pavlentygood.cellcapture.domain

import java.util.*

const val DEFAULT_PLAYER_LIMIT = 4

class PartyFactory(
    private val generatePlayerId: GeneratePlayerId
) {
    fun create(ownerName: PlayerName) =
        Party(
            id = PartyId(UUID.randomUUID()),
            playerLimit = PlayerLimit(DEFAULT_PLAYER_LIMIT),
            players = mutableListOf(createOwner(ownerName)),
            status = Party.Status.NEW,
            currentPlayerId = null,
            dicePair = DicePair(
                first = Dice.nonRolled(),
                second = Dice.nonRolled()
            ),
            field = Field(
                cells = createCells()
            )
        )

    private fun createOwner(ownerName: PlayerName) =
        Player(
            id = generatePlayerId(),
            name = ownerName,
            owner = true
        )

    private fun createCells() =
        Array(Field.HEIGHT) {
            Array(Field.WIDTH) { Field.nonePlayerId }
        }
}
