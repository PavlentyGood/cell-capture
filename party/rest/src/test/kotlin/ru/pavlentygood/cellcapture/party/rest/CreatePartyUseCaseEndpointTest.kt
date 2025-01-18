package ru.pavlentygood.cellcapture.party.rest

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.party.domain.PlayerId
import ru.pavlentygood.cellcapture.party.domain.partyId
import ru.pavlentygood.cellcapture.party.domain.playerName
import ru.pavlentygood.cellcapture.party.domain.randomInt
import ru.pavlentygood.cellcapture.party.usecase.CreatePartyResult
import ru.pavlentygood.cellcapture.party.usecase.CreatePartyUseCase

@WebMvcTest
@ContextConfiguration(classes = [CreatePartyUseCaseEndpointTest.Config::class, CreatePartyEndpoint::class])
class CreatePartyUseCaseEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var createParty: CreatePartyUseCase

    @Test
    fun `create party`() {
        val partyId = partyId()
        val uuid = partyId.toUUID().toString()
        val ownerId = randomInt()
        val ownerName = playerName()

        every { createParty(ownerName) } returns CreatePartyResult(partyId, PlayerId(ownerId))

        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(ownerName.toStringValue()))
        }.andExpect {
                status { isCreated() }
                content {
                    header {
                        string(HttpHeaders.LOCATION, "$API_V1_PARTIES/$uuid")
                    }
                    jsonPath("$.id") { value(uuid) }
                    jsonPath("$.ownerId") { value(ownerId) }
                }
            }
    }

    @Test
    fun `create party - invalid owner name`() {
        val invalidName = "Bo"
        mockMvc.post(API_V1_PARTIES) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(CreatePartyRequest(invalidName))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Configuration
    class Config {
        @Bean
        fun createParty() = mockk<CreatePartyUseCase>()
    }
}
