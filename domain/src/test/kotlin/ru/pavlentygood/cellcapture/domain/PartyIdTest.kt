package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.UUID

class PartyIdTest {

    @Test
    fun `create party id`() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        PartyId(id1) shouldBe PartyId(id1)
        PartyId(id1) shouldNotBe PartyId(id2)
        PartyId(id1).toUUID() shouldBe id1
    }
}
