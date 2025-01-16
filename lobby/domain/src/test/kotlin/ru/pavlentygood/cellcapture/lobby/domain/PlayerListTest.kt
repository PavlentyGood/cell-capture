package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class PlayerListTest {

    @Test
    fun `create player list`() {
        val player = player()

        val result = PlayerList.create(firstPlayer = player)
        result.players shouldContainExactly listOf(player)
    }

    @Test
    fun `restore player list`() {
        val player = player()
        val players = listOf(player)

        val result = PlayerList.restore(
            players = players
        )

        result.players shouldContainExactly players
    }

    @Test
    fun `add player`() {
        val firstPlayer = player()
        val player = player()
        val playerList = PlayerList.create(firstPlayer = firstPlayer)

        playerList.add(player)

        playerList.players shouldContainExactly listOf(firstPlayer, player)
    }
}
