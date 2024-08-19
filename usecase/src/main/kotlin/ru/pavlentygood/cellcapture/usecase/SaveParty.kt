package ru.pavlentygood.cellcapture.usecase

import ru.pavlentygood.cellcapture.domain.Party

fun interface SaveParty {
    operator fun invoke(party: Party)
}
