package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.domain.PlayerId

class GeneratePlayerIdBySequence(
    private var counter: Int = 0
) : GeneratePlayerId {
    override fun invoke() =
        PlayerId(++counter)
}
