package ru.pavlentygood.cellcapture.game.domain

const val DICE_CORRECTION = 1

abstract class Dices {

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
    }
}

data class RolledDices(
    val first: Dice,
    val second: Dice
) : Dices() {

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

    override val rolled = false

    override fun isNotMatched(area: Area) = true
}
