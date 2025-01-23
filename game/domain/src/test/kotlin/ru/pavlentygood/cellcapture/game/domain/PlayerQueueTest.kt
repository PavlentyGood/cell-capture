package ru.pavlentygood.cellcapture.game.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.get

class PlayerQueueTest {

    @Test
    fun `create player queue`() {
        val playerList = playerList()

        val result = PlayerQueue.create(playerList)
        result.players shouldContainExactly playerList.players
        result.currentPlayerId shouldBe playerList.ownerId
    }

    @Test
    fun `restore player queue`() {
        val playerList = playerList()
        val currentPlayerId = playerList.players.random().id

        val result = PlayerQueue.restore(
            playerList = playerList,
            currentPlayerId = currentPlayerId
        ).shouldBeRight()

        result.players shouldContainExactly playerList.players
        result.currentPlayerId shouldBe currentPlayerId
    }

    @Test
    fun `change current player`() {
        val currentPlayer = player()
        val nextPlayer = player()

        fun test(players: List<Player>) {
            val playerQueue = PlayerQueue.restore(
                playerList = playerList(
                    ownerId = currentPlayer.id,
                    players = players
                ),
                currentPlayerId = currentPlayer.id
            ).get()

            playerQueue.changeCurrentPlayer()

            playerQueue.players shouldContainExactly players
            playerQueue.currentPlayerId shouldBe nextPlayer.id
        }

        test(listOf(currentPlayer, nextPlayer))
        test(listOf(nextPlayer, currentPlayer))
    }
}
