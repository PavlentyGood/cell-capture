package io.github.pavlentygood.cellcapture.ai.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.*

class ForestIdTest {

    @Test
    fun `forest id exposes value`() {
        val uuid = UUID.randomUUID()
        ForestId(uuid).toUUID() shouldBe uuid
    }

    @Test
    fun `forest ids with same value are equal`() {
        val uuid = UUID.randomUUID()
        ForestId(uuid) shouldBe ForestId(uuid)
    }

    @Test
    fun `forest ids with different values differ`() {
        ForestId(UUID.randomUUID()) shouldNotBe ForestId(UUID.randomUUID())
    }
}
