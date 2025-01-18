package ru.pavlentygood.cellcapture.party.domain

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PartyInfoTest {

    @Test
    fun `get owner id`() {
        val owner = owner()
        val partyInfo = partyInfo(
            players = listOf(owner, player())
        )

        partyInfo.ownerId shouldBe owner.id
    }

    @Test
    fun `get player ids`() {
        val owner = owner()
        val player = player()
        val partyInfo = partyInfo(
            players = listOf(owner, player)
        )

        partyInfo.playerIds shouldContainExactly listOf(owner.id, player.id)
    }
}
