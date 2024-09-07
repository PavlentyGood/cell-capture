package ru.pavlentygood.cellcapture.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GeneratePlayerIdBySequenceTest {

    @Test
    fun `generate player id`() {
        val counter = 1
        val generatePlayerId = GeneratePlayerIdBySequence(counter)
        generatePlayerId().toInt() shouldBe counter + 1
    }
}
