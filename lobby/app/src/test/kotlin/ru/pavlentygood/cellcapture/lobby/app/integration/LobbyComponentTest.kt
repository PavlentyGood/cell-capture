package ru.pavlentygood.cellcapture.lobby.app.integration

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.app.integration.config.BaseKafkaTest
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyStartedEventDto
import ru.pavlentygood.cellcapture.lobby.rest.api.*
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.mapper
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.with
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@Import(value = [LobbyComponentTest.TestConsumerConfig::class])
@Sql(
    statements = ["truncate table outbox"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class LobbyComponentTest : BasePostgresTest, BaseKafkaTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getParty: GetParty

    companion object {
        lateinit var latch: CountDownLatch
        var sentPartyStartedEvent: PartyStartedEventDto? = null
    }

    @BeforeEach
    fun before() {
        latch = CountDownLatch(1)
        sentPartyStartedEvent = null
    }

    @RepeatedTest(2)
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
        sentPartyStartedEvent!!.partyId shouldBe party.id.toUUID()
        sentPartyStartedEvent!!.ownerId shouldBe party.ownerId.toInt()
        sentPartyStartedEvent!!.players.map { it.id } shouldBe party.getPlayers().map { it.id.toInt() }
        sentPartyStartedEvent!!.players.map { it.name } shouldBe party.getPlayers().map { it.name.toStringValue() }
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

    @TestConfiguration
    class TestConsumerConfig {
        @Bean
        fun partyStarted() = { message: PartyStartedEventDto ->
            sentPartyStartedEvent = message
            latch.countDown()
        }
    }
}
