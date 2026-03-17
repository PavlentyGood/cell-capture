package io.github.pavlentygood.cellcapture.kernel.domain.base

abstract class AggregateRoot<T, E : DomainEvent> protected constructor(
    id: T,
    version: Version
) : DomainEntity<T>(id) {

    var version: Version = version
        private set

    private val events = mutableListOf<E>()

    protected fun addEvent(event: E) {
        if (events.isEmpty()) {
            version = version.next()
        }
        events.add(event)
    }

    fun popEvents(): List<DomainEvent> {
        val result = events.toList()
        events.clear()
        return result
    }
}
