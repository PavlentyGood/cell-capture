package ru.pavlentygood.cellcapture.rest

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.PlayerName
import ru.pavlentygood.cellcapture.usecase.JoinPlayer
import ru.pavlentygood.cellcapture.usecase.PartyNotFoundUseCaseError
import ru.pavlentygood.cellcapture.usecase.PlayerCountLimitExceededUseCaseError
import java.util.*

@WebMvcTest
@ContextConfiguration(classes = [JoinPlayerEndpointTest.Config::class, JoinPlayerEndpoint::class])
class JoinPlayerEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var joinPlayer: JoinPlayer

    private val mapper = ObjectMapper()

    private val playerName = "Bob"

    @Test
    fun `join player`() {
        val playerId = 1
        val partyId = rawPartyId()

        every {
            joinPlayer(PartyId(partyId), PlayerName.from(playerName).getOrNull()!!)
        } returns PlayerId(playerId).right()

        post(partyId) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName))
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.id") { value(playerId) }
            }
        }
    }

    @Test
    fun `join player - party not found`() {
        val partyId = rawPartyId()

        every { joinPlayer(PartyId(partyId), any()) } returns PartyNotFoundUseCaseError.left()

        post(partyId) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName))
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `join player - partyId is invalid`() {
        post("invalid-party-id") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `join player - player name is invalid`() {
        val invalidName = ""

        post(rawPartyId()) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = invalidName))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `join player - player count limit is exceeded`() {
        val partyId = rawPartyId()

        every { joinPlayer(PartyId(partyId), any()) } returns PlayerCountLimitExceededUseCaseError.left()

        post(partyId) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = playerName))
        }.andExpect {
            status { isUnprocessableEntity() }
        }
    }

    private fun post(partyId: Any, dsl: MockHttpServletRequestDsl.() -> Unit) =
        mockMvc.post(
            urlTemplate = API_V1_PARTIES_PLAYERS.with("partyId", partyId),
            dsl = dsl
        )

    @Configuration
    class Config {
        @Bean
        fun joinPlayer() = mockk<JoinPlayer>()
    }
}

fun String.with(name: String, value: Any) =
    replace("{$name}", value.toString())

fun rawPartyId() = UUID.randomUUID()!!
