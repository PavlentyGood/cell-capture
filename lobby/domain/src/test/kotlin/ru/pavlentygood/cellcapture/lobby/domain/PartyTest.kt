package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName

class PartyTest {

    @Test
    fun `join player`() {
        val owner = player()
        val player = player()
        val party = party(owner = owner)

        val generatePlayerId = { player.id }

        party.joinPlayer(player.name, generatePlayerId) shouldBeRight player.id

        party.started shouldBe false
        party.getPlayers() shouldBe listOf(owner, player)
    }

    @Test
    fun `join player - limit`() {
        val owner = player()
        val player = player()
        val party = party(
            owner = owner,
            otherPlayers = listOf(player)
        )

        val generatePlayerId = { playerId() }

        party.joinPlayer(playerName(), generatePlayerId) shouldBeLeft Party.PlayerCountLimit

        party.started shouldBe false
        party.getPlayers() shouldBe listOf(owner, player)
    }

    @Test
    fun `start party`() {
        val owner = player()
        val player = player()
        val party = party(
            owner = owner,
            otherPlayers = listOf(player)
        )

        party.start(owner.id) shouldBeRight Unit

        party.started shouldBe true
        party.getPlayers() shouldBe listOf(owner, player)
    }

    @Test
    fun `start party - player not owner`() {
        val owner = player()
        val player = player()
        val party = party(
            owner = owner,
            otherPlayers = listOf(player)
        )

        party.start(playerId()) shouldBeLeft Party.PlayerNotOwner

        party.started shouldBe false
        party.getPlayers() shouldBe listOf(owner, player)
    }

    @Test
    fun `start party - too few players`() {
        val owner = player()
        val party = party(
            owner = owner
        )

        party.start(owner.id) shouldBeLeft Party.TooFewPlayers

        party.started shouldBe false
        party.getPlayers() shouldBe listOf(owner)
    }

    @Test
    fun `start party - already started`() {
        val owner = player()
        val player = player()
        val party = party(
            started = true,
            owner = owner,
            otherPlayers = listOf(player)
        )

        party.start(owner.id) shouldBeLeft Party.AlreadyStarted

        party.started shouldBe true
        party.getPlayers() shouldBe listOf(owner, player)
    }
}
