package ru.pavlentygood.cellcapture.game.usecase.port

import ru.pavlentygood.cellcapture.game.domain.AbstractParty

fun interface SaveParty : (AbstractParty) -> Unit
