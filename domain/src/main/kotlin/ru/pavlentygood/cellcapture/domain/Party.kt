package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.Party.Status.*

class Party internal constructor(
    val id: PartyId,
    val playerLimit: PlayerLimit,
    status: Status,
    dicePair: DicePair?,
    val field: Field,
    val playerQueue: PlayerQueue,
    val ownerId: PlayerId
) {
    var status = status
        private set

    var dicePair = dicePair
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
                    NEW -> Unit.right().also { start() }
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

    private fun start() {
        field.appointStartCells(players.map { it.id })
        status = STARTED
    }

    private fun isEnoughPlayers() =
        players.size >= MIN_PLAYER_COUNT

    fun roll(playerId: PlayerId): Either<Roll, DicePair> =
        if (playerId == playerQueue.currentPlayerId) {
            if (!dicePair.isRolled()) {
                dicePair = DicePair.roll()
                dicePair!!.right()
            } else {
                DicesAlreadyRolled.left()
            }
        } else {
            PlayerNotCurrent.left()
        }

    fun capture(playerId: PlayerId, area: Area): Either<Capture, Unit> =
        if (playerId == playerQueue.currentPlayerId) {
            if (dicePair.isRolled()) {
                if (dicePair!!.isMatched(area)) {
                    field.capture(playerId, area)
                        .onRight {
                            dicePair = null
                            playerQueue.changeCurrentPlayer()
                        }
                } else {
                    MismatchedArea.left()
                }
            } else {
                DicesNotRolled.left()
            }
        } else {
            PlayerNotCurrent.left()
        }

    private fun DicePair?.isRolled() =
        this != null

    enum class Status {
        NEW, STARTED, COMPLETED
    }

    sealed class Start
    data object PlayerNotOwner : Start()
    data object TooFewPlayers : Start()
    data object AlreadyStarted : Start()
    data object AlreadyCompleted : Start()

    sealed interface Roll
    data object DicesAlreadyRolled : Roll

    sealed class Capture
    data object PlayerNotCurrent : Capture(), Roll
    data object DicesNotRolled : Capture()
    data object MismatchedArea : Capture()
    data object InaccessibleArea : Capture()
}

data object PlayerCountLimitExceeded
