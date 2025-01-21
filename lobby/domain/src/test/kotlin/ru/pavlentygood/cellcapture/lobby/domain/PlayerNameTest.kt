package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PlayerNameTest {

    @ParameterizedTest
    @ValueSource(strings = ["Alice", "  Alice  "])
    fun `create player name`(name: String) {
        PlayerName.from(name) shouldBeRight PlayerName("Alice")
        PlayerName(name).toStringValue() shouldBe name
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "aa", "very long player name"])
    fun `create player name - invalid`(name: String) {
        PlayerName.from(name) shouldBeLeft InvalidPlayerName
    }
}
