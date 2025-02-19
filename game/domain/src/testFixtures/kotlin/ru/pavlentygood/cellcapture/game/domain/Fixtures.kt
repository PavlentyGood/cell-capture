package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.*

fun party(
    id: PartyId = partyId(),
    owner: Player = player(),
    currentPlayer: Player = player(),
    otherPlayers: List<Player> = listOf(currentPlayer),
    dices: Dices = dices(),
    field: Field = field()
) =
    ActiveParty(
        id = id,
        dices = dices,
        field = field,
        ownerId = owner.id,
        currentPlayerId = currentPlayer.id,
        players = listOf(owner).plus(otherPlayers)
    )

fun player(id: PlayerId = playerId()) =
    Player(
        id = id,
        name = playerName()
    )

fun area(distanceToEdges: Int = 0) =
    Area.from(
        first = point(distanceToEdges),
        second = point(distanceToEdges)
    )

fun point(x: Int, y: Int) =
    Point.from(
        x = x,
        y = y
    ).get()

fun point(distanceToEdges: Int = 0) =
    point(
        x = randomInt(from = distanceToEdges, until = Field.WIDTH - distanceToEdges),
        y = randomInt(from = distanceToEdges, until = Field.HEIGHT - distanceToEdges)
    )

fun dices() =
    Dices.roll()

fun field(cells: Array<Array<Cell>> = cells()) =
    Field(
        cells = cells
    )

fun cells(): Array<Array<Cell>> =
    createCells()

fun Array<Array<Cell>>.setCell(playerId: PlayerId, x: Int, y: Int) {
    this[y][x] = Cell(playerId, x, y)
}

fun Array<Array<Cell>>.capturedCells(): List<Cell> =
    flatten().filter { it.playerId != Field.nonePlayerId }

fun Array<Array<Cell>>.capturedCellCount() =
    capturedCells().count()

fun partyInfo(
    ownerId: PlayerId = playerId(),
    players: List<Player> = listOf(player(ownerId), player())
) =
    PartyInfo(
        partyId = partyId(),
        playerList = playerList(
            ownerId = ownerId,
            players = players
        )
    )

fun playerList(
    ownerId: PlayerId = playerId(),
    players: List<Player> = listOf(player(ownerId), player(playerId()))
) =
    PlayerList.from(
        ownerId = ownerId,
        players = players
    ).get()
