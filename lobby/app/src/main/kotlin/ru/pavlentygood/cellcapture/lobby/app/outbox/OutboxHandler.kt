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
        log.info("Handle outbox: id=${record.id}, eventType=${record.eventType}, body=${record.body}")
        kafkaTemplate.send(record.getTopic(), record.body)
        outboxRepository.markAsSent(record.id)
    }
}

fun OutboxReadDto.getTopic() =
    when (eventType) {
        EventTypeDto.PARTY_STARTED -> PARTY_STARTED_TOPIC
    }
