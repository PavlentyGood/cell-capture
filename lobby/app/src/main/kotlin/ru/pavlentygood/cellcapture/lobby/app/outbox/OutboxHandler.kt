package ru.pavlentygood.cellcapture.lobby.app.outbox

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.lobby.persistence.OutboxRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxReadDto
import ru.pavlentygood.cellcapture.lobby.app.PARTY_STARTED_TOPIC

@Component
class OutboxHandler(
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    @Scheduled(fixedDelay = 1000)
    @Transactional
    fun handleOutbox() {
        val record = outboxRepository.getNextRecord() ?: return
        val topic = record.getTopic()
        kafkaTemplate.send(topic, record.body)
        outboxRepository.markAsSent(record.id)
    }
}

fun OutboxReadDto.getTopic() =
    when (eventType) {
        "PartyStarted" -> PARTY_STARTED_TOPIC
        else -> error("Illegal event type")
    }
