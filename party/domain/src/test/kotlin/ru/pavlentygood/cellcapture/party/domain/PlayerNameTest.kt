package ru.pavlentygood.cellcapture.party.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PlayerNameTest {

    @ParameterizedTest
    @ValueSource(strings = ["Alice", "  Alice  "])
    fun `create player name`(name: String) {
        ru.pavlentygood.cellcapture.party.domain.PlayerName.Companion.from(name) shouldBeRight ru.pavlentygood.cellcapture.party.domain.PlayerName(
            "Alice"
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "aa", "very long player name"])
    fun `create player name - invalid`(name: String) {
        ru.pavlentygood.cellcapture.party.domain.PlayerName.Companion.from(name) shouldBeLeft ru.pavlentygood.cellcapture.party.domain.InvalidPlayerName
    }
}
