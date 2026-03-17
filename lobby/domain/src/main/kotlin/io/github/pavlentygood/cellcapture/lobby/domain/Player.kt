package io.github.pavlentygood.cellcapture.lobby.domain

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName

data class Player(
    val id: PlayerId,
    val name: PlayerName
)
