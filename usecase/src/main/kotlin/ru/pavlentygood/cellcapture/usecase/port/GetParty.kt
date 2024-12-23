package ru.pavlentygood.cellcapture.usecase.port

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId

fun interface GetParty {
    operator fun invoke(partyId: PartyId): Party?
}
