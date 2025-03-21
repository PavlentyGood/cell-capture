package ru.pavlentygood.cellcapture.game.listening

import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
@Import(value = [PartyStartedListener::class])
class ListeningConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, PartyStartedMessage> {
        val properties = mapOf(BOOTSTRAP_SERVERS_CONFIG to bootstrapServers)
        val jsonDeserializer = JsonDeserializer(PartyStartedMessage::class.java)
        jsonDeserializer.addTrustedPackages("*")
        val listenerFactory = ConcurrentKafkaListenerContainerFactory<String, PartyStartedMessage>()
        listenerFactory.consumerFactory = DefaultKafkaConsumerFactory(
            properties,
            StringDeserializer(),
            jsonDeserializer
        )
        return listenerFactory
    }
}
