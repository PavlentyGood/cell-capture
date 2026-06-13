package io.github.pavlentygood.cellcapture.lobby.app.usecase.port

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.domain.Party

fun interface GetPartyByPlayer : (PlayerId) -> Party?
