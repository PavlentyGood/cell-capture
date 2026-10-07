package io.github.pavlentygood.cellcapture.ai.app.usecase.port

import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestId

fun interface GetForest : (ForestId) -> Forest?
