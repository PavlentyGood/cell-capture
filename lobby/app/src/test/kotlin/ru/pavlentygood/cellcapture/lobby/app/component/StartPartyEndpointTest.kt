package ru.pavlentygood.cellcapture.lobby.app.component

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player
import ru.pavlentygood.cellcapture.lobby.persistence.*
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyStartedEventDto
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES_START
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.StartPartyEndpoint
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@SpringBootTest(classes = [
    StartPartyEndpointTest::class, StartPartyEndpoint::class])
@EnableAutoConfiguration
@AutoConfigureMockMvc
@EnableJpaRepositories("ru.pavlentygood.cellcapture.lobby.persistence")
@EntityScan("ru.pavlentygood.cellcapture.lobby.persistence")
@Import(
    value = [
        StartPartyUseCase::class,
        SavePartyToDatabase::class,
        GetPartyByPlayerFromDatabase::class,
        MapPartyToDomain::class,
        RestoreParty::class,
        ObjectMapper::class
    ]
)
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
        record.eventType shouldBe "PartyStarted"
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

    private fun startParty(playerId: PlayerId) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toInt().toString())
        }
}
