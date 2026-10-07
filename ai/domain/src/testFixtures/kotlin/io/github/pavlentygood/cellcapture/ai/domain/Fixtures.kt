package io.github.pavlentygood.cellcapture.ai.domain

import io.github.pavlentygood.cellcapture.kernel.domain.get
import io.github.pavlentygood.cellcapture.kernel.domain.randomInt
import java.util.*

fun forestId(value: UUID = UUID.randomUUID()) = ForestId(value)

fun forestName(value: String = "x".repeat(randomInt(from = 1, until = MAX_FOREST_NAME_LENGTH + 1))) =
    ForestName.of(value).get()

fun forest(id: ForestId = forestId(), name: ForestName = forestName()) =
    Forest.create(id, name)
