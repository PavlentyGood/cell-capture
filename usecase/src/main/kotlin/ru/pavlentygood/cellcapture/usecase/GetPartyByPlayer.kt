package ru.pavlentygood.cellcapture.usecase

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
