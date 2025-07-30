package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

data class Cell(
    val playerId: PlayerId,
    val x: Int,
    val y: Int
)
