package io.github.pavlentygood.cellcapture.lobby.usecase.port

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.domain.Party

fun interface GetParty : (PartyId) -> Party?
