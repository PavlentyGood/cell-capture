package ru.pavlentygood.cellcapture.usecase

import ru.pavlentygood.cellcapture.domain.PartyId

fun interface CreateParty {
    operator fun invoke(): PartyId
}
