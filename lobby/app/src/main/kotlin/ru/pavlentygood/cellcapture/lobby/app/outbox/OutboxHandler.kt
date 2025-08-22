package ru.pavlentygood.cellcapture.lobby.app.outbox

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.kernel.common.log
import ru.pavlentygood.cellcapture.lobby.app.PARTY_STARTED_TOPIC
import ru.pavlentygood.cellcapture.lobby.persistence.OutboxRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.EventTypeDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxReadDto

@Component
class OutboxHandler(
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    @Scheduled(fixedDelay = 1000)
    @Transactional
    fun handleOutbox() {
        val record = outboxRepository.getNextRecord() ?: return
        log.info("Handle outbox: id={}, eventType={}, body={}", record.id, record.eventType, record.body)
        kafkaTemplate.send(record.getTopic(), record.body).get() // убрать get()!
        outboxRepository.markAsSent(record.id)
        log.info("Outbox record was sent: id={}", record.id)
    }
}

fun OutboxReadDto.getTopic() =
    when (eventType) {
        EventTypeDto.PARTY_STARTED -> PARTY_STARTED_TOPIC
    }
