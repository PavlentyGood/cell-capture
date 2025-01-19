package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import arrow.core.right

class PlayerQueue internal constructor(
    val players: List<Player>,
    currentPlayerId: PlayerId
) {
    var currentPlayerId = currentPlayerId
        private set

    fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }

    companion object {

        fun create(playerList: PlayerList) =
            PlayerQueue(
                players = playerList.players,
                currentPlayerId = playerList.ownerId
            )

        fun restore(
            playerList: PlayerList,
            currentPlayerId: PlayerId
        ) =
            if (playerList.playerIds.contains(currentPlayerId)) {
                PlayerQueue(
                    players = playerList.players,
                    currentPlayerId = currentPlayerId
                ).right()
            } else {
                IllegalCurrentPlayerId.left()
            }
    }

    sealed interface PlayerQueueError
    data object IllegalCurrentPlayerId : PlayerQueueError
}
