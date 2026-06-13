package io.github.pavlentygood.cellcapture.kernel.domain.base

import io.github.pavlentygood.cellcapture.kernel.domain.version
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AggregateRootTest {

    @Test
    fun `aggregate event works`() {
        val aggregate = TestAggregate(id = 777, version = version(5))

        aggregate.testCommand()

        aggregate.version shouldBe version(6)
        aggregate.popEvents() shouldContainExactly listOf(TestCommandInvokedEvent(777))
        aggregate.popEvents() shouldHaveSize 0
    }
}

class TestAggregate(
    id: Int,
    version: Version
) : AggregateRoot<Int, DomainEvent>(id, version) {

    fun testCommand() {
        addEvent(TestCommandInvokedEvent(aggregateId = id))
    }
}

data class TestCommandInvokedEvent(val aggregateId: Int) : DomainEvent
