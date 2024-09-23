package ru.pavlentygood.cellcapture.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.Party.Status.*

class Party internal constructor(
    val id: PartyId,
    val playerLimit: PlayerLimit,
    private val players: MutableList<Player>,
    status: Status,
    currentPlayerId: PlayerId?,
    private val dicePair: DicePair,
    private val field: Field
) {
    var status = status
        private set

    val ownerId get() = players.first { it.owner }.id

    var currentPlayerId = currentPlayerId ?: ownerId
        private set

    fun getPlayers() = players.toList()
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

    fun capture(playerId: PlayerId, area: Area): Either<Capture, Unit> =
        if (playerId == currentPlayerId) {
            if (dicePair.isMatched(area)) {
                field.capture(playerId, area)
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
