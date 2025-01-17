package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.lobby.domain.PlayerId

class GeneratePlayerIdBySequence(
    private var counter: Int = 0
) : GeneratePlayerId {
    override fun invoke() =
        PlayerId(++counter)
}
