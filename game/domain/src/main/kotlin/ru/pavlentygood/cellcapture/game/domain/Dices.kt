package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right

const val DICE_CORRECTION = 1

abstract class Dices {

    abstract fun roll(): Either<Party.DicesAlreadyRolled, RolledDices>
    abstract fun isMatched(area: Area): Either<Party.DicesNotRolled, Boolean>

    companion object {
        fun notRolled() = NotRolledDices
    }
}

data class RolledDices(
    val first: Dice,
    val second: Dice
) : Dices() {

    override fun roll() =
        Party.DicesAlreadyRolled.left()

    override fun isMatched(area: Area) =
        (area.isMatched(first, second) || area.isMatched(second, first)).right()

    private fun Area.isMatched(a: Dice, b: Dice) =
        a.isMatched(xDistance()) && b.isMatched(yDistance())

    private fun Dice.isMatched(distance: Int) =
        value - DICE_CORRECTION == distance
}

data object NotRolledDices : Dices() {

    override fun roll() =
        RolledDices(
            first = Dice.roll(),
            second = Dice.roll()
        ).right()

    override fun isMatched(area: Area) =
        Party.DicesNotRolled.left()
}
