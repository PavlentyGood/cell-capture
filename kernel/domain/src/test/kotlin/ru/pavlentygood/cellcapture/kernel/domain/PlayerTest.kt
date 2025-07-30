package ru.pavlentygood.cellcapture.kernel.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class PlayerTest {

    @Test
    fun `create player`() {
        val id = playerId()
        val name = playerName()
        Player(id, name) shouldBe Player(id, name)
        player() shouldNotBe player()
    }
}
