package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class PlayerIdTest {

    @Test
    fun `create player id`() {
        val id1 = 1
        PlayerId(id1) shouldBe PlayerId(id1)
        PlayerId(id1).toInt() shouldBe id1
        playerId() shouldNotBe playerId()
    }
}
