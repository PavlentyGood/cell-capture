package ru.pavlentygood.cellcapture.domain

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
            currentPlayer: Player
        ) =
            if (players.contains(currentPlayer)) {
                PlayerQueue(
                    players = players.toMutableList(),
                    currentPlayerId = currentPlayer.id
                ).right()
            } else {
                InvalidPlayerQueue.left()
            }
    }

    data object InvalidPlayerQueue
}
