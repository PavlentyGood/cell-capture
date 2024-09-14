package ru.pavlentygood.cellcapture.rest

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
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.partyId
import ru.pavlentygood.cellcapture.domain.playerName
import ru.pavlentygood.cellcapture.domain.randomInt
import ru.pavlentygood.cellcapture.usecase.CreateParty
import ru.pavlentygood.cellcapture.usecase.CreatePartyResult

@WebMvcTest
@ContextConfiguration(classes = [CreatePartyEndpointTest.Config::class, CreatePartyEndpoint::class])
class CreatePartyEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var createParty: CreateParty

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

    // create party - invalid owner name

    @Configuration
    class Config {
        @Bean
        fun createParty() = mockk<CreateParty>()
    }
}
