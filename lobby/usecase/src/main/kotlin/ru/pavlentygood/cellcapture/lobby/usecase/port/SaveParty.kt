package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.lobby.domain.Party

fun interface SaveParty : (Party) -> Unit
