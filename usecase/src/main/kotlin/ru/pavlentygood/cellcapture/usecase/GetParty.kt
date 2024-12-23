package ru.pavlentygood.cellcapture.usecase

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId

fun interface GetParty {
    operator fun invoke(partyId: PartyId): Party?
}
