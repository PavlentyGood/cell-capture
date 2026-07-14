package io.github.pavlentygood.cellcapture.kernel.domain.base

import io.github.pavlentygood.cellcapture.kernel.domain.version
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AggregateRootTest {

    @Test
    fun `aggregate event works`() {
        val id = 777
        val aggregate = TestAggregate(
            id = id,
            version = version(5),
            events = listOf(TestAggregateCreatedEvent(id))
        )

        aggregate.testCommand()

        aggregate.version shouldBe version(6)
        aggregate.popEvents() shouldContainExactly
                listOf(TestAggregateCreatedEvent(id), TestCommandInvokedEvent(id))
        aggregate.popEvents() shouldHaveSize 0
    }
}

class TestAggregate(
    id: Int,
    version: Version,
    events: List<TestAggregateEvent>
) : AggregateRoot<Int, TestAggregateEvent>(id, version, events) {

    fun testCommand() {
        addEvent(TestCommandInvokedEvent(aggregateId = id))
    }
}

sealed interface TestAggregateEvent : DomainEvent
data class TestAggregateCreatedEvent(val aggregateId: Int) : TestAggregateEvent
data class TestCommandInvokedEvent(val aggregateId: Int) : TestAggregateEvent
