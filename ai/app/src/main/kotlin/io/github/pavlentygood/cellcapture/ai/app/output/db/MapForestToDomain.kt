package io.github.pavlentygood.cellcapture.ai.app.output.db

import arrow.core.getOrElse
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.ForestDto
import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestId
import io.github.pavlentygood.cellcapture.ai.domain.ForestName
import io.github.pavlentygood.cellcapture.ai.domain.restoreForest
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version

fun mapForestToDomain(dto: ForestDto): Forest =
    restoreForest(
        id = ForestId(dto.forestId),
        version = Version.from(dto.version).getOrElse {
            error("Illegal version: $it. version: ${dto.version}")
        },
        name = ForestName.of(dto.name).getOrElse {
            error("Illegal forest name: ${dto.name}")
        }
    )
