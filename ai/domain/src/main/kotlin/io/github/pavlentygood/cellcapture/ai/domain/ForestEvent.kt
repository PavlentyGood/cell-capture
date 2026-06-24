package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.base.DomainEvent

sealed interface ForestEvent : DomainEvent

data class ForestCreated(
    val id: ForestId,
    val name: ForestName
) : ForestEvent
