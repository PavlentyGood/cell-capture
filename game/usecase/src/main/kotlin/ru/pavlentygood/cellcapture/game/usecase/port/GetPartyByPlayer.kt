package ru.pavlentygood.cellcapture.game.usecase.port

import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
