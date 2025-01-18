package ru.pavlentygood.cellcapture.party.usecase.port

import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.PlayerId

fun interface GetPartyByPlayer : (PlayerId) -> Party?
