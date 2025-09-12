package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.game.domain.event.CellsCapturedEvent
import ru.pavlentygood.cellcapture.game.domain.event.DicesRolledEvent
import ru.pavlentygood.cellcapture.game.domain.event.PartyEvent
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError
import ru.pavlentygood.cellcapture.kernel.domain.base.Version

class ActiveParty internal constructor(
    events: List<PartyEvent> = listOf(),
    id: PartyId,
    version: Version,
    dices: Dices,
    private val field: Field,
    override val ownerId: PlayerId,
    currentPlayerId: PlayerId,
    override val players: List<Player>
) : Party(id, version) {

    init {
        events.forEach { addEvent(it) }
    }

    override val completed = false

    override var currentPlayerId = currentPlayerId
        private set

    override var dices = dices
        private set

    override val cells
        get() = field.getCells()

    override fun roll(playerId: PlayerId): Either<RollDicesError, RolledDices> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            dices.rolled -> DicesAlreadyRolled.left()
            else -> {
                val rolledDices = Dices.roll()
                dices = rolledDices
                addEvent(DicesRolledEvent(id, playerId, rolledDices))
                rolledDices.right()
            }
        }

    override fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit> =
        when {
            playerId != currentPlayerId -> PlayerNotCurrent.left()
            dices.notRolled -> DicesNotRolled.left()
            dices.isNotMatched(area) -> MismatchedArea.left()
            else -> {
                field.capture(playerId, area)
                    .onRight {
                        dices = Dices.notRolled()
                        changeCurrentPlayer()
                        addEvent(CellsCapturedEvent(id, playerId, area))
                    }
            }
        }

    private fun changeCurrentPlayer() {
        val currentIndex = players.indexOfFirst { it.id == currentPlayerId }
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        currentPlayerId = nextPlayer.id
    }
}

sealed interface RollDicesError : DomainError
sealed interface CaptureCellsError : DomainError

data object DicesAlreadyRolled : RollDicesError

data object DicesNotRolled : CaptureCellsError
data object InaccessibleArea : CaptureCellsError
data object MismatchedArea : CaptureCellsError

data object PlayerNotCurrent : CaptureCellsError, RollDicesError
