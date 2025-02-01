package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot

class Party internal constructor(
    id: PartyId,
    completed: Boolean,
    dicePair: DicePair?,
    private val field: Field,
    val ownerId: PlayerId,
    currentPlayerId: PlayerId,
    players: List<Player>
) : AggregateRoot<PartyId>(id) {

    var completed = completed
        private set

    var dicePair = dicePair
        private set

    var currentPlayerId = currentPlayerId
        private set

    private val players = players.toMutableList()

    fun getPlayers() = players.toList()

    fun getCells() = field.getCells()

    fun roll(playerId: PlayerId): Either<RollDicesError, DicePair> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            dicePair.isRolled() -> DicesAlreadyRolled.left()
            else -> {
                dicePair = DicePair.roll()
                dicePair!!.right()
            }
        }

    fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            dicePair.isNotRolled() -> DicesNotRolled.left()
            !dicePair!!.isMatched(area) -> MismatchedArea.left()
            else -> field.capture(playerId, area)
                .onRight {
                    dicePair = null
                    changeCurrentPlayer()
                }
        }

    private fun DicePair?.isRolled() =
        this != null

    private fun DicePair?.isNotRolled() =
        !isRolled()

    private fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }

    sealed interface RollDicesError
    sealed interface CaptureCellsError

    data object DicesAlreadyRolled : RollDicesError
    data object PlayerNotCurrent : CaptureCellsError, RollDicesError
    data object DicesNotRolled : CaptureCellsError
    data object MismatchedArea : CaptureCellsError
    data object InaccessibleArea : CaptureCellsError
}
