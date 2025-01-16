package ru.pavlentygood.cellcapture.lobby.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.*

class PartyIdTest {

    @Test
    fun `create party id`() {
        val id1 = UUID.randomUUID()
        PartyId(id1) shouldBe PartyId(id1)
        PartyId(id1).toUUID() shouldBe id1
        partyId() shouldNotBe partyId()
    }
}
