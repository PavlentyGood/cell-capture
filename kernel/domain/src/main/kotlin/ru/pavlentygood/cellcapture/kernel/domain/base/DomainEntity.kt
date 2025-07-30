package ru.pavlentygood.cellcapture.kernel.domain.base

abstract class DomainEntity<T> protected constructor(
    val id: T
)
