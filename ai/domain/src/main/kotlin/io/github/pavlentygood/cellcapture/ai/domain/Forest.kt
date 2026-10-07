package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version

class Forest internal constructor(
    id: ForestId,
    version: Version,
    events: List<ForestEvent>,
    val name: ForestName
) : AggregateRoot<ForestId, ForestEvent>(id, version, events) {

    companion object {

        fun create(id: ForestId, name: ForestName): Forest {
            return Forest(
                events = listOf(ForestCreated(id, name)),
                id = id,
                version = Version.new(),
                name = name
            )
        }
    }
}
