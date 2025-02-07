package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

class Party internal constructor(
    id: PartyId,
    dices: Dices,
    private val field: Field,
    val ownerId: PlayerId,
    currentPlayerId: PlayerId,
    players: List<Player>
) : AbstractParty(id) {

    var dices = dices
        private set

    var currentPlayerId = currentPlayerId
        private set

    private val players = players.toMutableList()

    fun getPlayers() = players.toList()

    fun getCells() = field.getCells()

    override fun roll(playerId: PlayerId): Either<RollDicesError, RolledDices> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            else -> dices.roll()
                .onRight { rolledDices ->
                    dices = rolledDices
                }
        }

    override fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            else -> dices.isMatched(area)
                .flatMap { matched ->
                    if (matched) {
                        field.capture(playerId, area)
                            .onRight {
                                dices = Dices.notRolled()
                                changeCurrentPlayer()
                            }
                    } else {
                        MismatchedArea.left()
                    }
                }
        }

    private fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }

    sealed interface RollDicesError : DomainError
    sealed interface CaptureCellsError : DomainError

    data object DicesAlreadyRolled : RollDicesError
    data object PlayerNotCurrent : CaptureCellsError, RollDicesError
    data object DicesNotRolled : CaptureCellsError
    data object MismatchedArea : CaptureCellsError
    data object InaccessibleArea : CaptureCellsError
}
