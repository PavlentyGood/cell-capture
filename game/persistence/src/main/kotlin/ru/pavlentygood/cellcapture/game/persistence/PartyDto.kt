package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.game.domain.Dices
import java.util.UUID

data class PartyDto(
    val id: UUID,
    val completed: Boolean,
    val firstDice: Int?,
    val secondDice: Int?,
    val ownerId: Int,
    val currentPlayerId: Int
) {
    fun restoreDices(): Dices {
        return Dices.restore(firstDice, secondDice).getOrElse {
            error("Invalid dice values: $firstDice and $secondDice")
        }
    }
}
