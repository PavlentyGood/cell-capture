package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Party internal constructor(
    val id: PartyId,
    started: Boolean,
    val playerLimit: PlayerLimit,
    private val playerList: PlayerList,
    val ownerId: PlayerId
) {
    var started = started
        private set

    val players by playerList::players

    fun joinPlayer(name: PlayerName, generatePlayerId: GeneratePlayerId) =
        if (playerLimit.isReached(players.size)) {
            PlayerCountLimit.left()
        } else {
            val player = Player(
                id = generatePlayerId(),
                name = name,
                owner = false
            )
            playerList.add(player)
            player.id.right()
        }

    fun start(playerId: PlayerId): Either<Start, Unit> {
        return if (isEnoughPlayers()) {
            if (playerId == ownerId) {
                if (started) {
                    AlreadyStarted.left()
                } else {
                    started = true
                    Unit.right()
                }
            } else {
                PlayerNotOwner.left()
            }
        } else {
            TooFewPlayers.left()
        }
    }

    private fun isEnoughPlayers() =
        players.size >= MIN_PLAYER_COUNT

    sealed interface Start

    data object PlayerCountLimit
    data object PlayerNotOwner : Start
    data object TooFewPlayers : Start
    data object AlreadyStarted : Start
}
