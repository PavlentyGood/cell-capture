package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.Party.Status.*

class Party internal constructor(
    val id: PartyId,
    val playerLimit: PlayerLimit,
    private val players: MutableList<Player>,
    status: Status
) {
    var status = status
        private set

    fun getPlayers() = players.toList()

    fun joinPlayer(name: PlayerName, generatePlayerId: GeneratePlayerId) =
        if (playerLimit.isExceeded(players.size)) {
            PlayerCountLimitExceeded.left()
        } else {
            val player = Player(
                id = generatePlayerId(),
                name = name,
                owner = false
            )
            players.add(player)
            player.id.right()
        }

    fun start(playerId: PlayerId): Either<Start, Unit> {
        return if (isEnoughPlayers()) {
            if (isOwner(playerId)) {
                when (status) {
                    NEW -> Unit.right().also { status = STARTED }
                    STARTED -> AlreadyStarted.left()
                    COMPLETED -> AlreadyCompleted.left()
                }
            } else {
                PlayerNotOwner.left()
            }
        } else {
            TooFewPlayers.left()
        }
    }

    private fun isOwner(playerId: PlayerId) =
        players.find { it.id == playerId }?.owner ?: false

    private fun isEnoughPlayers() =
        players.size >= MIN_PLAYER_COUNT

    enum class Status {
        NEW, STARTED, COMPLETED
    }

    sealed class Start
    data object PlayerNotOwner : Start()
    data object TooFewPlayers : Start()
    data object AlreadyStarted : Start()
    data object AlreadyCompleted : Start()
}

data object PlayerCountLimitExceeded
