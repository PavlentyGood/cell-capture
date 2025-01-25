package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.pavlentygood.cellcapture.kernel.domain.MAX_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.MIN_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.playerId

class PlayerListTest {

    @Test
    fun `create player list`() {
        val owner = player()
        val player = player()
        PlayerList.from(
            ownerId = owner.id,
            players = listOf(owner, player)
        ) shouldBeRight PlayerList(
            ownerId = owner.id,
            players = listOf(owner, player)
        )
    }

    @Test
    fun `create player list - unmatched owner id`() {
        val owner = player()
        PlayerList.from(
            ownerId = playerId(),
            players = listOf(owner, player())
        ) shouldBeLeft IllegalOwnerId
    }

    @ParameterizedTest
    @ValueSource(ints = [MIN_PLAYER_COUNT - 1, MAX_PLAYER_COUNT + 1])
    fun `create player list - illegal player count`(count: Int) {
        val players = generateSequence { player() }.take(count).toList()
        PlayerList.from(
            ownerId = players.first().id,
            players = players
        ) shouldBeLeft IllegalPlayerCount
    }

    @Test
    fun `create player list - duplicate player ids`() {
        val playerId = playerId()
        PlayerList.from(
            ownerId = playerId,
            players = listOf(player(playerId), player(playerId))
        ) shouldBeLeft DuplicatePlayerIds
    }

    @Test
    fun `get owner id`() {
        val owner = player()
        val playerList = playerList(
            ownerId = owner.id,
            players = listOf(owner, player())
        )

        playerList.ownerId shouldBe owner.id
    }

    @Test
    fun `get player ids`() {
        val owner = player()
        val player = player()
        val playerList = playerList(
            ownerId = owner.id,
            players = listOf(owner, player)
        )

        playerList.playerIds shouldContainExactly listOf(owner.id, player.id)
    }
}
