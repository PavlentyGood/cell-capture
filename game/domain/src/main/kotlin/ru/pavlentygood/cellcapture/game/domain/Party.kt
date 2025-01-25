package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot

class Party internal constructor(
    id: PartyId,
    completed: Boolean,
    dicePair: DicePair?,
    private val field: Field,
    private val playerQueue: PlayerQueue,
    val ownerId: PlayerId
) : AggregateRoot<PartyId>(id) {

    var completed = completed
        private set

    var dicePair = dicePair
        private set

    val players by playerQueue::players

    val currentPlayerId by playerQueue::currentPlayerId

    fun getCells() = field.getCells()

    fun roll(playerId: PlayerId): Either<RollDicesError, DicePair> =
        when {
            playerId != playerQueue.currentPlayerId -> PlayerNotCurrent.left()
            dicePair.isRolled() -> DicesAlreadyRolled.left()
            else -> {
                dicePair = DicePair.roll()
                dicePair!!.right()
            }
        }

    fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit> =
        when {
            playerId != playerQueue.currentPlayerId -> PlayerNotCurrent.left()
            dicePair.isNotRolled() -> DicesNotRolled.left()
            !dicePair!!.isMatched(area) -> MismatchedArea.left()
            else -> field.capture(playerId, area)
                .onRight {
                    dicePair = null
                    playerQueue.changeCurrentPlayer()
                }
        }

    private fun DicePair?.isRolled() =
        this != null

    private fun DicePair?.isNotRolled() =
        !isRolled()

    sealed interface RollDicesError
    sealed interface CaptureCellsError

    data object DicesAlreadyRolled : RollDicesError
    data object PlayerNotCurrent : CaptureCellsError, RollDicesError
    data object DicesNotRolled : CaptureCellsError
    data object MismatchedArea : CaptureCellsError
    data object InaccessibleArea : CaptureCellsError
}
