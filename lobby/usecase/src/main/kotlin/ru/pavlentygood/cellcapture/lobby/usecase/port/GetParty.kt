package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.lobby.domain.Party

fun interface GetParty : (PartyId) -> Party?
