package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PartyId

fun interface GetParty : (PartyId) -> Party?
