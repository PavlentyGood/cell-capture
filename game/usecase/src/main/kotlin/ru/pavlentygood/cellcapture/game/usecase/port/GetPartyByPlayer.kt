package ru.pavlentygood.cellcapture.game.usecase.port

import ru.pavlentygood.cellcapture.game.domain.AbstractParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> AbstractParty?
