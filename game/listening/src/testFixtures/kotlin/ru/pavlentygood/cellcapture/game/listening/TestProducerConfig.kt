package ru.pavlentygood.cellcapture.game.listening

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@TestConfiguration
class TestProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, PartyStartedMessage> {
        val properties = mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers)
        val serializer = JsonSerializer<PartyStartedMessage>()
        serializer.noTypeInfo()
        val factory = DefaultKafkaProducerFactory(
            properties,
            StringSerializer(),
            serializer
        )
        return KafkaTemplate(factory)
    }

    @Bean
    fun partyStartedTopic(): NewTopic =
        TopicBuilder
            .name(PARTY_STARTED_TOPIC)
            .build()
}
