package ru.pavlentygood.cellcapture.kernel.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.*

class PartyIdTest {

    @Test
    fun `create party id`() {
        val value = UUID.randomUUID()
        PartyId(value) shouldBe PartyId(value)
        PartyId(value).toUUID() shouldBe value
        partyId() shouldNotBe partyId()
    }
}
