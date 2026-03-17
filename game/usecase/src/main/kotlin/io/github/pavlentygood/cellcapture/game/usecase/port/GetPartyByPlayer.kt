package io.github.pavlentygood.cellcapture.game.usecase.port

import io.github.pavlentygood.cellcapture.game.domain.Party
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
