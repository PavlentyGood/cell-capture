package ru.pavlentygood.cellcapture.lobby.app

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

const val PARTY_STARTED_TOPIC = "party-started"

@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val properties = mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers)
        val factory = DefaultKafkaProducerFactory(
            properties,
            StringSerializer(),
            StringSerializer()
        )
        return KafkaTemplate(factory)
    }

    @Bean
    fun partyStartedTopic(): NewTopic =
        TopicBuilder
            .name(PARTY_STARTED_TOPIC)
            .build()
}
