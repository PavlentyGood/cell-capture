package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class RestorePartyTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 2])
    fun `restore party`(playerCount: Int) {
        val players = generateSequence { player() }
            .take(playerCount)
            .toList()
        val party = party(
            playerLimit = 2,
            owner = players.first(),
            otherPlayers = players.drop(1)
        )
        val restoreParty = RestoreParty()

        val restoredParty: Party = restoreParty(
            id = party.id,
            started = party.started,
            playerLimit = party.playerLimit,
            players = party.getPlayers(),
            ownerId = party.ownerId
        ).shouldBeRight()

        restoredParty.apply {
            id shouldBe party.id
            started shouldBe party.started
            playerLimit shouldBe party.playerLimit
            getPlayers() shouldBe party.getPlayers()
            ownerId shouldBe party.ownerId
        }
    }

    @Test
    fun `restore party - too many players`() {
        val limit = 2
        val restoreParty = RestoreParty()
        val players = generateSequence { player() }
            .take(limit + 1)
            .toList()

        restoreParty(
            id = partyId(),
            started = false,
            playerLimit = playerLimit(limit),
            players = players,
            ownerId = players.first().id
        ) shouldBeLeft TooManyPlayers
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun `restore party - too few players`(partyStarted: Boolean) {
        val playerCount = if (partyStarted) 1 else 0
        val restoreParty = RestoreParty()
        val players = generateSequence { player() }
            .take(playerCount)
            .toList()

        restoreParty(
            id = partyId(),
            started = partyStarted,
            playerLimit = playerLimit(),
            players = players,
            ownerId = playerId(),
        ) shouldBeLeft TooFewPlayers
    }

    @Test
    fun `restore party - illegal owner id`() {
        val restoreParty = RestoreParty()

        restoreParty(
            id = partyId(),
            started = false,
            playerLimit = playerLimit(),
            players = listOf(player()),
            ownerId = playerId(),
        ) shouldBeLeft IllegalOwnerId
    }
}
