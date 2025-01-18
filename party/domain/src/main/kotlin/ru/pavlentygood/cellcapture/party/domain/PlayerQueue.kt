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

    fun add(player: Player) {
        _players.add(player)
    }

    fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }

    companion object {

        fun create(firstPlayer: Player) =
            PlayerQueue(
                players = mutableListOf(firstPlayer),
                currentPlayerId = firstPlayer.id
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
