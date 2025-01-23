package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party

fun interface GetPartyByPlayer : (PlayerId) -> Party?
