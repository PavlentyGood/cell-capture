package ru.pavlentygood.cellcapture.party.domain

import arrow.core.left
import arrow.core.right

class PlayerQueue internal constructor(
    players: MutableList<Player>,
    currentPlayerId: PlayerId
) {
    private val _players = players
    val players: List<Player> get() = _players

    var currentPlayerId = currentPlayerId
        private set

    fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }

    companion object {

        fun create(partyInfo: PartyInfo) =
            PlayerQueue(
                players = partyInfo.players.toMutableList(),
                currentPlayerId = partyInfo.ownerId
            )

        fun restore(
            players: List<Player>,
            currentPlayerId: PlayerId
        ) =
            if (players.map { it.id }.contains(currentPlayerId)) {
                PlayerQueue(
                    players = players.toMutableList(),
                    currentPlayerId = currentPlayerId
                ).right()
            } else {
                InvalidPlayerQueue.left()
            }
    }

    data object InvalidPlayerQueue
}
