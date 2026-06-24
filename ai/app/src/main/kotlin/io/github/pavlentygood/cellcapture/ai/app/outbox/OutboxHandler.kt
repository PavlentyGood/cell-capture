package io.github.pavlentygood.cellcapture.ai.app.outbox

import io.github.pavlentygood.cellcapture.ai.app.output.db.OutboxRepository
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.EventTypeDto
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.OutboxReadDto
import io.github.pavlentygood.cellcapture.kernel.common.log
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OutboxHandler(
    private val outboxRepository: OutboxRepository,
    private val streamBridge: StreamBridge
) {
    @Scheduled(fixedDelay = 1000)
    @Transactional
    fun handleOutbox() {
        val record = outboxRepository.getNextRecord() ?: return
        log.info("Handle outbox: id={}, eventType={}, body={}", record.id, record.eventType, record.body.value)
        streamBridge.send(record.getBinding(), record.body.value)
        outboxRepository.markAsSent(record.id)
        log.info("Outbox record sent: id={}", record.id)
    }
}

fun OutboxReadDto.getBinding() =
    when (eventType) {
        EventTypeDto.FOREST_CREATED -> "forestCreated-out-0"
    }
