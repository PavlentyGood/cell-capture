package io.github.pavlentygood.cellcapture.lobby.domain

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

fun interface GeneratePlayerId : () -> PlayerId
