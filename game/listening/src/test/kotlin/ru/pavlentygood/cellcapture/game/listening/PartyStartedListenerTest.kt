package ru.pavlentygood.cellcapture.game.listening

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest(
    classes = [TestConfig::class, ListeningConfig::class, TestProducerConfig::class]
)
class PartyStartedListenerTest : BaseKafkaTest {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, PartyStartedMessage>
    @Autowired
    lateinit var createParty: CreatePartyUseCase

    val latch = CountDownLatch(1)

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
        val partyStartedMessage = PartyStartedMessage(
            partyId = partyId.toUUID(),
            ownerId = owner.id.toInt(),
            players = listOf(
                PartyStartedMessage.Player(
                    id = owner.id.toInt(),
                    name = owner.name.toStringValue()
                ),
                PartyStartedMessage.Player(
                    id = player.id.toInt(),
                    name = player.name.toStringValue()
                )
            )
        )

        Thread.sleep(1000 * 5) // костыль

        every { createParty(partyInfo) } answers {
            latch.countDown()
        }

        kafkaTemplate.send(PARTY_STARTED_TOPIC, partyStartedMessage)

        latch.await(10, TimeUnit.SECONDS) shouldBe true
    }

    @Configuration
    class TestConfig {
        @Bean
        fun createPartyUseCase(): CreatePartyUseCase = mockk<CreatePartyUseCase>()
    }
}
