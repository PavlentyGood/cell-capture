package io.github.pavlentygood.cellcapture.ai.app.output.db

import io.github.pavlentygood.cellcapture.ai.app.usecase.port.GetForest
import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestId

class GetForestFromDatabase(
    private val forestRepository: ForestRepository
) : GetForest {

    override fun invoke(forestId: ForestId): Forest? =
        forestRepository.findById(forestId.toUUID())
            .map { mapForestToDomain(it) }
            .orElse(null)
}
