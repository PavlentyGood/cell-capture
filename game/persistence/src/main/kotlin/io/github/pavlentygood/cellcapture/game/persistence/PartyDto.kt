package io.github.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import io.github.pavlentygood.cellcapture.game.domain.Dices
import java.util.*

data class PartyDto(
    val id: UUID,
    val version: Long,
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
