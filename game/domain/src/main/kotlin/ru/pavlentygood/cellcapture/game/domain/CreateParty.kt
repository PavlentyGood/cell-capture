package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class CreateParty {

    private val widthLimit = Field.WIDTH - 1
    private val heightLimit = Field.HEIGHT - 1
    private val widthHalf = Field.WIDTH / 2
    private val heightHalf = Field.HEIGHT / 2
    private val widthQuarter = Field.WIDTH / 4
    private val heightQuarter = Field.HEIGHT / 4
    private val widthThreeQuarter = Field.WIDTH / 4 * 3
    private val heightThreeQuarter = Field.HEIGHT / 4 * 3

    private val playerStartCells: List<Point> = listOf(
        point(0, 0),
        point(widthLimit, heightLimit),
        point(widthLimit, 0),
        point(0, heightLimit),
        point(widthHalf, 0),
        point(widthHalf, heightLimit),
        point(0, heightHalf),
        point(widthLimit, heightHalf),
        point(widthQuarter, 0),
        point(widthThreeQuarter, heightLimit),
        point(widthLimit, heightQuarter),
        point(0, heightThreeQuarter),
        point(widthThreeQuarter, 0),
        point(widthQuarter, heightLimit),
        point(0, heightQuarter),
        point(widthLimit, heightThreeQuarter)
    )

    private fun point(x: Int, y: Int) =
        Point.from(x, y).getOrNull()!!

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

    private fun generateStartCells(playerIds: List<PlayerId>): List<Cell> =
        playerIds.zip(playerStartCells) { playerId, point ->
            Cell(playerId, point.x, point.y)
        }
}
