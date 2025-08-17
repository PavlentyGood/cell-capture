package ru.pavlentygood.cellcapture.kernel.domain.base

abstract class AggregateRoot<T, E : DomainEvent> protected constructor(
    id: T
) : DomainEntity<T>(id) {

    private val events = mutableListOf<E>()

    fun getEvents() = events.toList()

    protected fun addEvent(event: E) {
        events.add(event)
    }
}
