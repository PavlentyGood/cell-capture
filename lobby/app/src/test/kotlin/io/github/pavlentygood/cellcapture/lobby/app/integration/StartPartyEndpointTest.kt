package io.github.pavlentygood.cellcapture.lobby.app.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.partyId
import io.github.pavlentygood.cellcapture.kernel.domain.playerId
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.domain.party
import io.github.pavlentygood.cellcapture.lobby.domain.player
import io.github.pavlentygood.cellcapture.lobby.app.output.db.OutboxRepository
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.EventTypeDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PartyStartedEventDto
import io.github.pavlentygood.cellcapture.lobby.restapi.API_V1_PARTIES_START
import io.github.pavlentygood.cellcapture.lobby.rest.endpoint.StartPartyEndpoint
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.SaveParty
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

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
        record.body.value shouldBe objectMapper.writeValueAsString(
            PartyStartedEventDto(
                partyId = partyId.toUUID(),
                ownerId = owner.id.toInt(),
                players = listOf(
                    PartyStartedEventDto.Player(
                        owner.id.toInt(),
                        owner.name.toStringValue()
                    ),
                    PartyStartedEventDto.Player(
                        player.id.toInt(),
                        player.name.toStringValue()
                    )
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

    private fun startParty(playerId: PlayerId) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toInt().toString())
        }
}
