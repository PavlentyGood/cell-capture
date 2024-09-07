package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class PartyFactoryTest {

    @Test
    fun `create party`() {
        val partyFactory = PartyFactory()

        val party: Party = partyFactory.create()

        party.id.toString() shouldContain "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"
            .toRegex()

        party.playerLimit shouldBe PlayerLimit.from(DEFAULT_PLAYER_LIMIT).getOrNull()!!
        party.getPlayers() shouldBe mutableListOf()
    }
}
