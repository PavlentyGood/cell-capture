package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.get
import io.github.pavlentygood.cellcapture.kernel.domain.version
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

class ForestTest {

    @Test
    fun `create forest`() {
        val id = ForestId(UUID.randomUUID())
        val name = ForestName.of("Taiga").get()

        val forest = Forest.create(id, name)

        forest.id shouldBe id
        forest.name shouldBe name
        forest.version shouldBe version(1)
        forest.popEvents() shouldContainExactly listOf(ForestCreated(id, name))
    }
}
