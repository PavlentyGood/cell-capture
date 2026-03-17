package io.github.pavlentygood.cellcapture.lobby.usecase.port

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.domain.Party

fun interface GetPartyByPlayer : (PlayerId) -> Party?
