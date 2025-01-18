package ru.pavlentygood.cellcapture.party.usecase.port

import ru.pavlentygood.cellcapture.party.domain.Party

fun interface SaveParty : (Party) -> Unit
