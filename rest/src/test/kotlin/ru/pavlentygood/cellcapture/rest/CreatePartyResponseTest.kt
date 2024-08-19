package ru.pavlentygood.cellcapture.rest

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.UUID

class CreatePartyResponseTest {

    @Test
    fun equals() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
        CreatePartyResponse(id = id1) shouldBe CreatePartyResponse(id = id1)
        CreatePartyResponse(id = id1) shouldNotBe CreatePartyResponse(id = id2)
    }
}
