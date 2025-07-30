package ru.pavlentygood.cellcapture.game.listening

import arrow.core.right
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import ru.pavlentygood.cellcapture.game.domain.partyInfo
import ru.pavlentygood.cellcapture.game.domain.player
import ru.pavlentygood.cellcapture.game.listening.PartyStartedListenerTest.TestConfig
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import kotlin.time.Duration.Companion.seconds

@SpringBootTest(
    classes = [TestConfig::class, ListeningConfig::class, TestProducerConfig::class]
)
class PartyStartedListenerTest : BaseKafkaTest {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, PartyStartedMessage>
    @Autowired
    lateinit var createParty: CreatePartyUseCase

    @Test
    fun `listen party started message`() {
        val partyId = partyId()
        val owner = player()
        val player = player()
        val partyInfo = partyInfo(
            partyId = partyId,
            ownerId = owner.id,
            players = listOf(owner, player)
        )
        val partyStartedMessage = partyStartedMessage(
            partyId = partyId,
            owner = owner,
            player = player
        )

        Thread.sleep(1000 * 5) // костыль

        kafkaTemplate.send(PARTY_STARTED_TOPIC, partyStartedMessage)

        runBlocking {
            eventually(
                eventuallyConfig {
                    duration = 10.seconds
                    initialDelay = 1.seconds
                }
            ) {
                every { createParty(partyInfo) } returns Unit.right()
            }
        }
    }

    @Configuration
    class TestConfig {
        @Bean
        fun createPartyUseCase(): CreatePartyUseCase = mockk<CreatePartyUseCase>()
    }
}
