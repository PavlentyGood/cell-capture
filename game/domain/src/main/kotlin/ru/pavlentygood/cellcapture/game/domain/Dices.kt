package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right

const val DICE_CORRECTION = 1

abstract class Dices {

    abstract val firstValue: Int?
    abstract val secondValue: Int?
    abstract val rolled: Boolean

    val notRolled get() = !rolled

    abstract fun isNotMatched(area: Area): Boolean

    companion object {

        fun notRolled() = NotRolledDices

        fun roll(): RolledDices =
            RolledDices(
                first = Dice.roll(),
                second = Dice.roll()
            )

        fun restore(first: Int?, second: Int?): Either<Dice.InvalidValue, Dices> =
            when {
                first == null && second == null -> notRolled().right()
                first != null && second != null -> restore(first, second)
                else -> Dice.InvalidValue.left()
            }

        private fun restore(first: Int, second: Int): Either<Dice.InvalidValue, Dices> =
            Dice.from(first).flatMap { firstDice ->
                Dice.from(second).map { secondDice ->
                    RolledDices(firstDice, secondDice)
                }
            }
    }
}

data class RolledDices(
    val first: Dice,
    val second: Dice
) : Dices() {

    override val firstValue = first.value
    override val secondValue = second.value
    override val rolled = true

    override fun isNotMatched(area: Area) = !isMatched(area)

    private fun isMatched(area: Area) =
        area.isMatched(first, second) || area.isMatched(second, first)

    private fun Area.isMatched(a: Dice, b: Dice) =
        a.isMatched(xDistance()) && b.isMatched(yDistance())

    private fun Dice.isMatched(distance: Int) =
        value - DICE_CORRECTION == distance
}

data object NotRolledDices : Dices() {

    override val firstValue = null
    override val secondValue = null
    override val rolled = false

    override fun isNotMatched(area: Area) = true
}
