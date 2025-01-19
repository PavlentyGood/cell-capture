package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PartyInfoTest {

    @Test
    fun `create party info`() {
        val partyId = partyId()
        val owner = player()
        val player = player()
        PartyInfo.from(
            id = partyId,
            ownerId = owner.id,
            players = listOf(owner, player)
        ) shouldBeRight PartyInfo(
            id = partyId,
            ownerId = owner.id,
            players = listOf(owner, player)
        )
    }

    @Test
    fun `create party info - unmatched owner id`() {
        val owner = player()
        PartyInfo.from(
            id = partyId(),
            ownerId = playerId(),
            players = listOf(owner, player())
        ) shouldBeLeft UnmatchedOwnerId
    }

    @ParameterizedTest
    @ValueSource(ints = [MIN_PLAYER_COUNT - 1, MAX_PLAYER_COUNT + 1])
    fun `create party info - illegal player count`(count: Int) {
        val players = generateSequence { player() }.take(count).toList()
        PartyInfo.from(
            id = partyId(),
            ownerId = players.first().id,
            players = players
        ) shouldBeLeft IllegalPlayerCount
    }

    @Test
    fun `create party info - duplicate player ids`() {
        val playerId = playerId()
        PartyInfo.from(
            id = partyId(),
            ownerId = playerId,
            players = listOf(player(playerId), player(playerId))
        ) shouldBeLeft DuplicatePlayerIds
    }

    @Test
    fun `get owner id`() {
        val owner = player()
        val partyInfo = partyInfo(
            ownerId = owner.id,
            players = listOf(owner, player())
        )

        partyInfo.ownerId shouldBe owner.id
    }

    @Test
    fun `get player ids`() {
        val owner = player()
        val player = player()
        val partyInfo = partyInfo(
            ownerId = owner.id,
            players = listOf(owner, player)
        )

        partyInfo.playerIds shouldContainExactly listOf(owner.id, player.id)
    }
}
