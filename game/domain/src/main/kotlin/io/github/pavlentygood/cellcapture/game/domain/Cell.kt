package io.github.pavlentygood.cellcapture.game.domain

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

data class Cell(
    val playerId: PlayerId,
    val x: Int,
    val y: Int
)
