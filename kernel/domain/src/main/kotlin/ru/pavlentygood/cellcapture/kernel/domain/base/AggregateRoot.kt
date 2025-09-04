package ru.pavlentygood.cellcapture.kernel.domain.base

abstract class AggregateRoot<T, E : DomainEvent> protected constructor(
    id: T
) : DomainEntity<T>(id) {

    private val events = mutableListOf<E>()

    fun popEvents(): List<DomainEvent> {
        val result = events.toList()
        events.clear()
        return result
    }

    protected fun addEvent(event: E) {
        events.add(event)
    }
}
