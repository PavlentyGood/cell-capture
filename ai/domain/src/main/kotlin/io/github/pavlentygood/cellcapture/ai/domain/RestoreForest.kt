package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.base.Version

fun restoreForest(
    id: ForestId,
    version: Version,
    name: ForestName
): Forest =
    Forest(
        id = id,
        version = version,
        events = listOf(),
        name = name
    )
