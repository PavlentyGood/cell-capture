package ru.pavlentygood.cellcapture.usecase.port

import ru.pavlentygood.cellcapture.domain.Party

fun interface SaveParty : (Party) -> Unit
