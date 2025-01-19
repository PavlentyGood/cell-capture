package ru.pavlentygood.cellcapture.game.usecase.port

import ru.pavlentygood.cellcapture.game.domain.Party

fun interface SaveParty : (Party) -> Unit
