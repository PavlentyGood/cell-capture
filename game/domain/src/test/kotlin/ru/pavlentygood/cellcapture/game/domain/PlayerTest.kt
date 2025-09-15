package ru.pavlentygood.cellcapture.game.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName

class PlayerTest {

    @Test
    fun `create player`() {
        val id = playerId()
        val name = playerName()
        Player(id, name) shouldBe Player(id, name)
        player() shouldNotBe player()
    }
}
