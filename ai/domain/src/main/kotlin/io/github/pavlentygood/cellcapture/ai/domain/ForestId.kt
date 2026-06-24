package io.github.pavlentygood.cellcapture.ai.domain

import java.util.*

data class ForestId(
    private val value: UUID
) {
    fun toUUID() = value
}
