package ru.pavlentygood.cellcapture.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class PlayerIdTest {

    @Test
    fun `create player id`() {
        val id1 = 1
        val id2 = 2
        PlayerId(id1) shouldBe PlayerId(id1)
        PlayerId(id1) shouldNotBe PlayerId(id2)
        PlayerId(id1).toInt() shouldBe id1
    }
}
