package ru.pavlentygood.cellcapture.game.domain

data class DicePair(
    val first: Dice,
    val second: Dice
) {
    fun isMatched(area: Area) =
        area.isMatched(first, second) || area.isMatched(second, first)

    private fun Area.isMatched(a: Dice, b: Dice) =
        a.isMatched(xDistance()) && b.isMatched(yDistance())

    private fun Dice.isMatched(distance: Int) =
        value - CORRECTION == distance

    companion object {
        const val CORRECTION = 1

        fun roll() =
            DicePair(
                first = Dice.roll(),
                second = Dice.roll()
            )
    }
}
