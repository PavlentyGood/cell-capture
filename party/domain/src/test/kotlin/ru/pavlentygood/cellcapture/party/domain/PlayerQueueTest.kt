package ru.pavlentygood.cellcapture.party.domain

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PlayerQueueTest {

    @Test
    fun `create player queue`() {
        val partyInfo = partyInfo()

        val result = PlayerQueue.create(partyInfo)
        result.players shouldContainExactly partyInfo.players
        result.currentPlayerId shouldBe partyInfo.ownerId
    }

    @Test
    fun `restore player queue`() {
        val player = player()
        val players = listOf(player)

        val result = PlayerQueue.restore(
            players = players,
            currentPlayerId = player.id
        ).shouldBeRight()

        result.players shouldContainExactly players
        result.currentPlayerId shouldBe player.id
    }

    @Test
    fun `change current player`() {
        val currentPlayer = player()
        val nextPlayer = player()

        fun test(players: List<Player>) {
            val playerQueue = PlayerQueue.restore(
                players = players,
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
