package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.randomInt
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ForestNameTest {

    val forestNameMaxLength = 100

    @Test
    fun `forest name exposes value`() {
        val value = "x".repeat(randomInt(from = 1, until = forestNameMaxLength + 1))
        ForestName.of(value).shouldBeRight().toText() shouldBe value
    }

    @Test
    fun `forest name with max length`() {
        val value = "x".repeat(forestNameMaxLength)
        ForestName.of(value).shouldBeRight()
    }

    @Test
    fun `forest name is blank`() {
        ForestName.of("") shouldBeLeft ForestNameError.Blank
        ForestName.of(" ") shouldBeLeft ForestNameError.Blank
    }

    @Test
    fun `forest name is too long`() {
        val value = "x".repeat(forestNameMaxLength + 1)
        ForestName.of(value) shouldBeLeft ForestNameError.TooLong
    }
}
