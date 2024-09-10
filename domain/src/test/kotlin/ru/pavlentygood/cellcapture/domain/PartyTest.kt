package ru.pavlentygood.cellcapture.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyTest {

    @Test
    fun `join player`() {
        val playerId = playerId()
        val name = playerName()
        val party = party()

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeRight playerId

        party.getPlayers().map { it.id } shouldContainOnly listOf(playerId)
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `join player - limit`() {
        val playerId = playerId()
        val name = playerName()
        val party = party(playerLimit = 0)

        val generatePlayerId = { playerId }

        party.joinPlayer(name, generatePlayerId) shouldBeLeft PlayerCountLimitExceeded

        party.getPlayers() shouldHaveSize 0
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party`() {
        val (party, owner) = partyAndOwner()

        party.start(owner.id) shouldBeRight Unit

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - player not owner when player joined`() {
        val player = player()
        val party = party(players = listOf(player(), player()))

        party.start(player.id) shouldBeLeft Party.PlayerNotOwner

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - player not owner when player not joined`() {
        val player = player()
        val (party, _) = partyAndOwner()

        party.start(player.id) shouldBeLeft Party.PlayerNotOwner

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - too few players`() {
        val player = player(owner = true)
        val party = party(players = listOf(player))

        party.start(player.id) shouldBeLeft Party.TooFewPlayers

        party.getPlayers() shouldHaveSize 1
        party.status shouldBe Party.Status.NEW
    }

    @Test
    fun `start party - already started`() {
        val (party, owner) = partyAndOwner(status = Party.Status.STARTED)

        party.start(owner.id) shouldBeLeft Party.AlreadyStarted

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.STARTED
    }

    @Test
    fun `start party - already completed`() {
        val (party, owner) = partyAndOwner(status = Party.Status.COMPLETED)

        party.start(owner.id) shouldBeLeft Party.AlreadyCompleted

        party.getPlayers() shouldHaveSize 2
        party.status shouldBe Party.Status.COMPLETED
    }
}
