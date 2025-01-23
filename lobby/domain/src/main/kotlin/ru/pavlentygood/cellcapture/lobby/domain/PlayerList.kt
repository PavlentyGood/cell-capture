package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.Player

class PlayerList internal constructor(
    players: MutableList<Player>
) {
    private val _players = players
    val players: List<Player> get() = _players

    fun add(player: Player) {
        _players.add(player)
    }

    companion object {

        fun create(firstPlayer: Player) =
            PlayerList(
                players = mutableListOf(firstPlayer)
            )

        fun restore(
            players: List<Player>
        ) =
            PlayerList(
                players = players.toMutableList()
            )
    }
}
