package ru.pavlentygood.cellcapture.lobby.publishing

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class PublishingConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {
    @Bean
    fun <T> kafkaTemplate(): KafkaTemplate<String, T> {
        val properties = mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers)
        val serializer = JsonSerializer<T>()
        serializer.noTypeInfo()
        val factory = DefaultKafkaProducerFactory(
            properties,
            StringSerializer(),
            serializer
        )
        return KafkaTemplate(factory)
    }

    @Bean
    fun sendPartyStartedMessage() =
        SendPartyStartedMessage(kafkaTemplate())

    @Bean
    fun partyStartedTopic(): NewTopic =
        TopicBuilder
            .name(PARTY_STARTED_TOPIC)
            .build()
}
