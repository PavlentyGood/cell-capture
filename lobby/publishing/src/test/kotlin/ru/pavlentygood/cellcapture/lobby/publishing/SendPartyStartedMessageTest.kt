package ru.pavlentygood.cellcapture.lobby.publishing

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.annotation.KafkaListener
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [PublishingConfig::class, TestConsumerConfig::class])
class SendPartyStartedMessageTest : BaseKafkaTest {

    @Autowired
    private lateinit var sendPartyStartedMessage: SendPartyStartedMessage

    private val latch = CountDownLatch(1)
    private lateinit var consumedRecord: PartyDto

    @Test
    fun `publish party started event`() {
        val owner = player()
        val player = player()
        val party = party(
            owner = owner,
            otherPlayers = listOf(player)
        )

        sendPartyStartedMessage(party)

        latch.await(10, TimeUnit.SECONDS) shouldBe true
        consumedRecord.apply {
            partyId shouldBe party.id.toUUID()
            ownerId shouldBe owner.id.toInt()
            players.first().id shouldBe owner.id.toInt()
            players.first().name shouldBe owner.name.toStringValue()
            players.last().id shouldBe player.id.toInt()
            players.last().name shouldBe player.name.toStringValue()
        }
    }

    @KafkaListener(topics = [PARTY_STARTED_TOPIC], groupId = "test")
    fun testConsumer(record: PartyDto) {
        consumedRecord = record
        latch.countDown()
    }
}
