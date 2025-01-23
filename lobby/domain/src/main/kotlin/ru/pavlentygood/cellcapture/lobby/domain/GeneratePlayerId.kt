package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

fun interface GeneratePlayerId : () -> PlayerId
