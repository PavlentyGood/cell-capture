package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.pavlentygood.cellcapture.kernel.domain.randomInt

class PlayerLimitTest {

    @Test
    fun `create player limit`() {
        val value = randomPlayerCount()
        PlayerLimit.from(value) shouldBe PlayerLimit.from(value)
        PlayerLimit.from(value) shouldNotBe PlayerLimit.from(randomPlayerCount())
    }

    @ParameterizedTest
    @ValueSource(ints = [Int.MIN_VALUE, -1, 0, 1])
    fun `create player limit - invalid`(limit: Int) {
        PlayerLimit.from(limit) shouldBeLeft InvalidPlayerLimit
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
        randomInt(from = 2)
}
