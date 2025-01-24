package ru.pavlentygood.cellcapture.kernel.domain.base

abstract class AggregateRoot<T> protected constructor(
    id: T
) : DomainEntity<T>(id)
