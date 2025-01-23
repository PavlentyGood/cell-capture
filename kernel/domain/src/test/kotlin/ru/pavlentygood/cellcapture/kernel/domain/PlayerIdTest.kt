package ru.pavlentygood.cellcapture.kernel.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class PlayerIdTest {

    @Test
    fun `create player id`() {
        val value = randomInt()
        PlayerId(value) shouldBe PlayerId(value)
        PlayerId(value).toInt() shouldBe value
        playerId() shouldNotBe playerId()
    }
}
