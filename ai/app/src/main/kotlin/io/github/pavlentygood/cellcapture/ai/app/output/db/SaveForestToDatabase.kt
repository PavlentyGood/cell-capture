package io.github.pavlentygood.cellcapture.ai.app.output.db

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.EventTypeDto
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.ForestCreatedEventDto
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.ForestDto
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.OutboxDto
import io.github.pavlentygood.cellcapture.ai.app.usecase.port.SaveForest
import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestCreated
import io.github.pavlentygood.cellcapture.ai.domain.ForestEvent
import io.github.pavlentygood.cellcapture.ai.domain.ForestId
import org.postgresql.util.PGobject
import org.springframework.transaction.annotation.Transactional

@Transactional
class SaveForestToDatabase(
    private val forestRepository: ForestRepository,
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
) : SaveForest {

    override fun invoke(forest: Forest) {
        forestRepository.save(forest.toDto())

        forest.popEvents()
            .filterIsInstance<ForestEvent>()
            .map { it.toOutboxDto(forest.id, objectMapper) }
            .let { outboxRepository.saveAll(it) }
    }
}

fun Forest.toDto() =
    ForestDto(
        forestId = id.toUUID(),
        version = version.value,
        name = name.toText()
    )

fun ForestEvent.toOutboxDto(aggregateId: ForestId, om: ObjectMapper) =
    OutboxDto(
        aggregateId = aggregateId.toUUID().toString(),
        status = "PENDING",
        eventType = this.getType(),
        body = PGobject().apply {
            type = "json"
            value = om.writeValueAsString(this@toOutboxDto.toDto())
        }
    )

fun ForestEvent.getType() =
    when (this) {
        is ForestCreated -> EventTypeDto.FOREST_CREATED
    }

fun ForestEvent.toDto() =
    when (this) {
        is ForestCreated -> this.toDto()
    }

fun ForestCreated.toDto() =
    ForestCreatedEventDto(
        id = id.toUUID(),
        name = name.toText()
    )
