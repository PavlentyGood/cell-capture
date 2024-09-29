package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.Party.Status.*

class Party internal constructor(
    val id: PartyId,
    val playerLimit: PlayerLimit,
    status: Status,
    val dicePair: DicePair,
    val field: Field,
    val playerQueue: PlayerQueue,
    val ownerId: PlayerId
) {
    var status = status
        private set

    val players by playerQueue::players

    fun getCells() = field.getCells()

    fun joinPlayer(name: PlayerName, generatePlayerId: GeneratePlayerId) =
        if (playerLimit.isExceeded(players.size)) {
            PlayerCountLimitExceeded.left()
        } else {
            val player = Player(
                id = generatePlayerId(),
                name = name,
                owner = false
            )
            playerQueue.add(player)
            player.id.right()
        }

    fun start(playerId: PlayerId): Either<Start, Unit> {
        return if (isEnoughPlayers()) {
            if (playerId == ownerId) {
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

    private fun isEnoughPlayers() =
        players.size >= MIN_PLAYER_COUNT

    fun capture(playerId: PlayerId, area: Area): Either<Capture, Unit> =
        if (playerId == playerQueue.currentPlayerId) {
            if (dicePair.isMatched(area)) {
                field.capture(playerId, area)
                    .onRight { playerQueue.changeCurrentPlayer() }
            } else {
                MismatchedArea.left()
            }
        } else {
            PlayerNotCurrent.left()
        }

    enum class Status {
        NEW, STARTED, COMPLETED
    }

    sealed class Start
    data object PlayerNotOwner : Start()
    data object TooFewPlayers : Start()
    data object AlreadyStarted : Start()
    data object AlreadyCompleted : Start()

    sealed class Capture
    data object PlayerNotCurrent : Capture()
    data object MismatchedArea : Capture()
    data object InaccessibleArea : Capture()
}

data object PlayerCountLimitExceeded
