package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Party internal constructor(
    val id: PartyId,
    completed: Boolean,
    dicePair: DicePair?,
    private val field: Field,
    private val playerQueue: PlayerQueue,
    val ownerId: PlayerId
) {
    var completed = completed
        private set

    var dicePair = dicePair
        private set

    val players by playerQueue::players

    val currentPlayerId by playerQueue::currentPlayerId

    fun getCells() = field.getCells()

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

    sealed interface Roll
    sealed interface Capture

    data object DicesAlreadyRolled : Roll
    data object PlayerNotCurrent : Capture, Roll
    data object DicesNotRolled : Capture
    data object MismatchedArea : Capture
    data object InaccessibleArea : Capture
}
