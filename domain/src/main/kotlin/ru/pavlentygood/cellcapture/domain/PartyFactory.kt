package ru.pavlentygood.cellcapture.domain

import java.util.*

const val DEFAULT_PLAYER_LIMIT = 4

class PartyFactory(
    private val generatePlayerId: GeneratePlayerId
) {
    fun create(ownerName: PlayerName): Party {
        val owner = createOwner(ownerName)
        return Party(
            id = PartyId(UUID.randomUUID()),
            playerLimit = PlayerLimit(DEFAULT_PLAYER_LIMIT),
            status = Party.Status.NEW,
            dicePair = DicePair(
                first = Dice.nonRolled(),
                second = Dice.nonRolled()
            ),
            field = Field(
                cells = createCells()
            ),
            playerQueue = PlayerQueue.create(firstPlayer = owner),
            ownerId = owner.id
        )
    }

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
