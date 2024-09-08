package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Party internal constructor(
    val id: PartyId,
    val playerLimit: PlayerLimit,
    private val players: MutableList<Player>
) {
    fun getPlayers() = players.toList()

    fun joinPlayer(name: PlayerName, generatePlayerId: GeneratePlayerId) =
        if (playerLimit.isExceeded(players.size)) {
            PlayerCountLimitExceeded.left()
        } else {
            val player = Player(
                id = generatePlayerId(),
                name = name
            )
            players.add(player)
            player.id.right()
        }

    fun start(playerId: PlayerId): Either<Start, Unit> {
        TODO("Not yet implemented")
    }

    sealed class Start
    data object PlayerNotOwner : Start()
    data object TooFewPlayers : Start()
    data object AlreadyStarted : Start()
    data object AlreadyCompleted : Start()
}

data object PlayerCountLimitExceeded
