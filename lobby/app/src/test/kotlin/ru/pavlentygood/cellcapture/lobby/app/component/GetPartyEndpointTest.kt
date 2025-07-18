package ru.pavlentygood.cellcapture.lobby.app.component

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.domain.player
import ru.pavlentygood.cellcapture.lobby.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.lobby.persistence.GetPartyFromDatabase
import ru.pavlentygood.cellcapture.lobby.persistence.MapPartyToDomain
import ru.pavlentygood.cellcapture.lobby.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTY_BY_ID
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.GetPartyEndpoint
import ru.pavlentygood.cellcapture.lobby.rest.endpoint.with
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@SpringBootTest(classes = [GetPartyEndpointTest::class, GetPartyEndpoint::class])
@EnableAutoConfiguration
@AutoConfigureMockMvc
@EnableJpaRepositories("ru.pavlentygood.cellcapture.lobby.persistence")
@EntityScan("ru.pavlentygood.cellcapture.lobby.persistence")
@Import(
    value = [
        GetPartyUseCase::class,
        SavePartyToDatabase::class,
        GetPartyFromDatabase::class,
        MapPartyToDomain::class,
        RestoreParty::class
    ]
)
class GetPartyEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `get party`() {
        val owner = player()
        val partyId = partyId()
        val party = party(
            id = partyId,
            owner = owner
        )
        saveParty(party)

        mockMvc.get(API_V1_PARTY_BY_ID.with("partyId", partyId.toUUID()))
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id") { value(partyId.toUUID().toString()) }
                    jsonPath("$.started") { value(party.started) }
                    jsonPath("$.ownerId") { value(party.ownerId.toInt()) }
                    jsonPath("$.playerLimit") { value(party.playerLimit.value) }
                    jsonPath("$.players[0].id") { value(owner.id.toInt()) }
                    jsonPath("$.players[0].name") { value(owner.name.toStringValue()) }
                }
            }
    }

    @Test
    fun `party not found`() {
        mockMvc.get(API_V1_PARTY_BY_ID.with("partyId", partyId().toUUID()))
            .andExpect { status { isNotFound() } }
    }
}
