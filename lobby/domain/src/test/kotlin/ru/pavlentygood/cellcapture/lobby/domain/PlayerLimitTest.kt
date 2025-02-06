package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.pavlentygood.cellcapture.kernel.domain.MAX_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.MIN_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.randomInt

class PlayerLimitTest {

    @Test
    fun `create player limit`() {
        val value = randomPlayerCount()
        PlayerLimit.from(value) shouldBe PlayerLimit.from(value)
        PlayerLimit.from(2) shouldNotBe PlayerLimit.from(3)
    }

    @ParameterizedTest
    @ValueSource(ints = [MIN_PLAYER_COUNT - 1, MAX_PLAYER_COUNT + 1])
    fun `create player limit - illegal`(limit: Int) {
        PlayerLimit.from(limit) shouldBeLeft IllegalPlayerLimit
    }

    @Test
    fun `is reached`() {
        val playerCount = randomPlayerCount()
        PlayerLimit.from(playerCount).onRight {
            it.isReached(playerCount) shouldBe true
        }
    }

    @Test
    fun `is not reached`() {
        val playerCount = randomPlayerCount()
        PlayerLimit.from(playerCount + 1).onRight {
            it.isReached(playerCount) shouldBe false
        }
    }

    private fun randomPlayerCount() =
        randomInt(from = 2, until = MAX_PLAYER_COUNT + 1)
}
