package ru.pavlentygood.cellcapture.party.persistence

import ru.pavlentygood.cellcapture.party.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.party.domain.PlayerId

class GeneratePlayerIdBySequence(
    private var counter: Int = 0
) : GeneratePlayerId {
    override fun invoke() =
        PlayerId(++counter)
}
