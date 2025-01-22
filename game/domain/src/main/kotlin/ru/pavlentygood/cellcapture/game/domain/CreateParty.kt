package ru.pavlentygood.cellcapture.game.domain

import kotlin.random.Random

class CreateParty {

    operator fun invoke(partyInfo: PartyInfo): Party {
        return Party(
            id = partyInfo.partyId,
            completed = false,
            dicePair = null,
            field = createField(partyInfo.playerList),
            playerQueue = PlayerQueue.create(partyInfo.playerList),
            ownerId = partyInfo.ownerId
        )
    }

    private fun createField(playerList: PlayerList): Field {
        val cells = createCells()

        generateStartCells(playerList.playerIds)
            .forEach { cell -> cells[cell.y][cell.x] = cell.playerId }

        return Field(cells)
    }

    private fun createCells(): Array<Array<PlayerId>> =
        Array(Field.HEIGHT) {
            Array(Field.WIDTH) { Field.nonePlayerId }
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
