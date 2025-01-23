package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId

class GeneratePlayerIdBySequence(
    private var counter: Int = 0
) : GeneratePlayerId {
    override fun invoke() =
        PlayerId(++counter)
}
