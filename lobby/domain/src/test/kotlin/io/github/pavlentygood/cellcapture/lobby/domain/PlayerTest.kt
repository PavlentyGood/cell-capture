package io.github.pavlentygood.cellcapture.lobby.domain

import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.github.pavlentygood.cellcapture.kernel.domain.playerName
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class PlayerTest {

    @Test
    fun `create player`() {
        val id = playerId()
        val name = playerName()
        Player(
            id,
            name
        ) shouldBe Player(id, name)
        player() shouldNotBe player()
    }
}
