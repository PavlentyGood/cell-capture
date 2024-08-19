package ru.pavlentygood.cellcapture.rest

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.CreateParty
import java.util.*

@WebMvcTest
@ContextConfiguration(classes = [CreatePartyEndpointTest.Config::class, CreatePartyEndpoint::class])
class CreatePartyEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var createParty: CreateParty

    @Test
    fun `create party`() {
        val id = UUID.randomUUID()

        every { createParty() } returns PartyId(id)

        mockMvc.post(API_V1_PARTIES)
            .andExpect {
                status { isCreated() }
                content {
                    header {
                        string(HttpHeaders.LOCATION, "$API_V1_PARTIES/$id")
                    }
                    jsonPath("$.id") { value(id.toString()) }
                }
            }
    }

    @Configuration
    class Config {
        @Bean
        fun createParty() = mockk<CreateParty>()
    }
}
