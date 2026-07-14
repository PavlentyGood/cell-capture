package io.github.pavlentygood.cellcapture.kernel.domain.base

abstract class AggregateRoot<ID, EVENT : DomainEvent> protected constructor(
    id: ID,
    version: Version,
    events: List<EVENT>,
) : DomainEntity<ID>(id) {

    var version: Version = version
        private set

    private val events = mutableListOf<EVENT>()

    init {
        events.forEach { addEvent(it) }
    }

    protected fun addEvent(event: EVENT) {
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
