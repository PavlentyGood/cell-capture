package ru.pavlentygood.cellcapture.game.usecase.port

import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
