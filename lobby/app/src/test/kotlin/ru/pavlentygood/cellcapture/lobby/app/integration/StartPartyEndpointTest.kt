package ru.pavlentygood.cellcapture.lobby.app.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player
import ru.pavlentygood.cellcapture.lobby.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.persistence.OutboxRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.EventTypeDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyStartedEventDto
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES_START
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.StartPartyEndpoint
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@AutoConfigureMockMvc
@SpringBootTest(classes = [StartPartyEndpoint::class])
@Import(IntegrationConfig::class)
class StartPartyEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var saveParty: SaveParty
    @Autowired
    lateinit var getPartyByPlayer: GetPartyByPlayer
    @Autowired
    lateinit var outboxRepository: OutboxRepository
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `start party`() {
        val owner = player()
        val player = player()
        val partyId = partyId()
        val party = party(
            id = partyId,
            owner = owner,
            otherPlayers = listOf(player)
        )
        saveParty(party)

        startParty(owner.id).andExpect {
            status { isOk() }
        }

        val startedParty: Party = getPartyByPlayer(owner.id)!!
        startedParty.started shouldBe true
        startedParty.getPlayers() shouldBe party.getPlayers()
        startedParty.playerLimit shouldBe party.playerLimit
        startedParty.ownerId shouldBe party.ownerId

        val record = outboxRepository.findAll().single {
            it.aggregateId == partyId.toUUID().toString()
        }
        record.id shouldBeGreaterThan 0
        record.status shouldBe "PENDING"
        record.eventType shouldBe EventTypeDto.PARTY_STARTED
        record.body shouldBe objectMapper.writeValueAsString(
            PartyStartedEventDto(
                partyId = partyId.toUUID(),
                ownerId = owner.id.toInt(),
                players = listOf(
                    PartyStartedEventDto.Player(owner.id.toInt(), owner.name.toStringValue()),
                    PartyStartedEventDto.Player(player.id.toInt(), player.name.toStringValue())
                )
            )
        )
    }

    @Test
    fun `player not found`() {
        startParty(playerId()).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `player not owner`() {
        val owner = player()
        val player = player()
        val party = party(
            id = partyId(),
            owner = owner,
            otherPlayers = listOf(player)
        )
        saveParty(party)

        startParty(player.id).andExpect {
            status { isForbidden() }
        }
    }

    @Test
    fun `too few players`() {
        val owner = player()
        val party = party(
            id = partyId(),
            owner = owner
        )
        saveParty(party)

        startParty(owner.id).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `party already started`() {
        val owner = player()
        val party = party(
            id = partyId(),
            started = true,
            owner = owner,
            otherPlayers = listOf(player())
        )
        saveParty(party)

        startParty(owner.id).andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `version conflict when saving party`(): Unit = runBlocking {
        val owner = player()
        val party = party(owner = owner)
        saveParty(party)

        coroutineScope {
            val doRequest = {
                async(Dispatchers.Default) {
                    startParty(owner.id).andReturn().response.status
                }
            }
            val statuses = listOf(doRequest(), doRequest()).awaitAll()

            statuses shouldContain 409
        }
    }

    private fun startParty(playerId: PlayerId) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toInt().toString())
        }
}
