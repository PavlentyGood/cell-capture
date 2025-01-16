package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
