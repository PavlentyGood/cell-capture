package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import kotlin.random.Random

class CreateParty {

    operator fun invoke(partyInfo: PartyInfo): Party {
        return ActiveParty(
            id = partyInfo.partyId,
            dices = Dices.notRolled(),
            field = createField(partyInfo.playerList),
            ownerId = partyInfo.ownerId,
            currentPlayerId = partyInfo.ownerId,
            players = partyInfo.players
        )
    }

    private fun createField(playerList: PlayerList): Field {
        val cells = createCells()

        generateStartCells(playerList.playerIds)
            .forEach { cell ->
                cells[cell.y][cell.x] = Cell(cell.playerId, cell.x, cell.y)
            }

        return Field(cells)
    }

    private tailrec fun generateStartCells(
        playerIds: List<PlayerId>,
        cells: List<Cell> = listOf()
    ): List<Cell> =
        if (playerIds.isEmpty()) {
            cells
        } else {
            val cell = generateSequence {
                Cell(
                    playerId = playerIds.first(),
                    x = Random.nextInt(Field.WIDTH),
                    y = Random.nextInt(Field.HEIGHT)
                )
            }.first { cell ->
                cells.none { it.x == cell.x && it.y == cell.y }
            }

            generateStartCells(playerIds.drop(1), cells.plus(cell))
        }
}
