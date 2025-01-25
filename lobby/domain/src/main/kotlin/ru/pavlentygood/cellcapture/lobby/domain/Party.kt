package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.*
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot

class Party internal constructor(
    id: PartyId,
    started: Boolean,
    players: List<Player>,
    val playerLimit: PlayerLimit,
    val ownerId: PlayerId
) : AggregateRoot<PartyId>(id) {

    var started = started
        private set

    private val players = players.toMutableList()

    fun getPlayers() = players.toList()

    fun joinPlayer(name: PlayerName, generatePlayerId: GeneratePlayerId) =
        if (playerLimit.isReached(players.size)) {
            PlayerCountLimit.left()
        } else {
            val player = Player(
                id = generatePlayerId(),
                name = name
            )
            players.add(player)
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
