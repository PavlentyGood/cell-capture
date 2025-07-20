package ru.pavlentygood.cellcapture.lobby.app.component

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.publishing.BaseKafkaTest
import ru.pavlentygood.cellcapture.lobby.publishing.PARTY_STARTED_TOPIC
import ru.pavlentygood.cellcapture.lobby.publishing.PartyDto
import ru.pavlentygood.cellcapture.lobby.publishing.TestConsumerConfig
import ru.pavlentygood.cellcapture.lobby.rest.api.*
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.mapper
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.with
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
@AutoConfigureMockMvc
@Import(value = [TestConsumerConfig::class])
class LobbyComponentTest : BasePostgresTest, BaseKafkaTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getParty: GetParty

    companion object {
        lateinit var latch: CountDownLatch
        var sentStartedParty: PartyDto? = null
    }

    @BeforeEach
    fun before() {
        latch = CountDownLatch(1)
        sentStartedParty = null
    }

    @RepeatedTest(10)
    fun `all use cases as process`() {
        val ownerName = playerName()
        val playerName = playerName()

        val created: CreatePartyResponse = createParty(ownerName) // use case
        joinPlayer(created.id, playerName) // use case
        startParty(created.ownerId) // use case

        val party: Party = getParty(PartyId(created.id))!!
        party.started shouldBe true
        party.getPlayers().size shouldBe 2
        party.getPlayers().map { it.id.toInt() } shouldContain created.ownerId
        party.getPlayers().map { it.name } shouldContainExactly listOf(ownerName, playerName)

        latch.await(5, TimeUnit.SECONDS) shouldBe true
        sentStartedParty!!.partyId shouldBe party.id.toUUID()
    }

    private fun createParty(playerName: PlayerName) =
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(playerName.toStringValue()))
        }.andExpect { status { isCreated() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, CreatePartyResponse::class.java) }

    private fun joinPlayer(partyId: UUID, playerName: PlayerName) =
        mockMvc.post(API_V1_PARTIES_PLAYERS.with("partyId", partyId)) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName.toStringValue()))
        }.andExpect { status { isOk() } }
            .andReturn().response.contentAsString
            .let { mapper.readValue(it, JoinPlayerResponse::class.java) }
            .id

    private fun startParty(playerId: Int) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toString())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isOk() } }

    @KafkaListener(topics = [PARTY_STARTED_TOPIC], groupId = "test")
    fun testConsumer(record: PartyDto) {
        sentStartedParty = record
        latch.countDown()
    }
}
