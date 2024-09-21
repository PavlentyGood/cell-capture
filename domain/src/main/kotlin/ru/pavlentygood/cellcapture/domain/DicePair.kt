package ru.pavlentygood.cellcapture.domain

data class DicePair(
    val first: Dice,
    val second: Dice
) {
    fun isMatched(area: Area) =
        area.isMatched(first, second) || area.isMatched(second, first)

    private fun Area.isMatched(a: Dice, b: Dice) =
        a.value == xDistance() && b.value == yDistance()
}
