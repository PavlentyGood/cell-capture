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
    Party(
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
    Dices.notRolled().roll().get()

fun field(cells: Array<Array<PlayerId>> = cells()) =
    Field(
        cells = cells
    )

fun cells() =
    Array(Field.HEIGHT) {
        Array(Field.WIDTH) { Field.nonePlayerId }
    }

fun Array<Array<PlayerId>>.capturedCellCount() =
    sumOf { line ->
        line.count { id -> id != Field.nonePlayerId }
    }

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
    ownerId: PlayerId = playerId(until = 1000000),
    players: List<Player> = listOf(player(ownerId), player(playerId(until = 1000000)))
) =
    PlayerList.from(
        ownerId = ownerId,
        players = players
    ).get()
