package ru.pavlentygood.cellcapture.party.usecase.port

import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.PartyId

fun interface GetParty : (PartyId) -> Party?
