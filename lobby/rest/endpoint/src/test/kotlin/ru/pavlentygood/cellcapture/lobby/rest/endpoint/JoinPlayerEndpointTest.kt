package ru.pavlentygood.cellcapture.lobby.rest.endpoint

import arrow.core.left
import arrow.core.right
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
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES_PLAYERS
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import ru.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCaseError.*
import java.util.*

@WebMvcTest
@ContextConfiguration(classes = [JoinPlayerEndpointTest.Config::class, JoinPlayerEndpoint::class])
class JoinPlayerEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var joinPlayer: JoinPlayerUseCase

    private val playerName = playerName()
    private val rawPlayerName = playerName.toStringValue()

    @Test
    fun `join player`() {
        val playerId = playerId()
        val partyId = partyId()

        every {
            joinPlayer(partyId, playerName)
        } returns playerId.right()

        post(partyId.toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = rawPlayerName))
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.id") { value(playerId.toInt()) }
            }
        }
    }

    @Test
    fun `join player - party not found`() {
        val partyId = partyId()

        every { joinPlayer(partyId, any()) } returns PartyNotFoundUseCaseError.left()

        post(partyId.toUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = rawPlayerName))
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

        post(UUID.randomUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(JoinPlayerRequest(name = invalidName))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `join player - usecase errors`() {
        listOf(
            AlreadyStartedUseCaseError,
            PlayerCountLimitUseCaseError
        ).forEach { useCaseError ->
            val partyId = partyId()

            every { joinPlayer(partyId, any()) } returns useCaseError.left()

            post(partyId.toUUID()) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(JoinPlayerRequest(name = rawPlayerName))
            }.andExpect {
                status { isUnprocessableEntity() }
            }
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
        fun joinPlayer() = mockk<JoinPlayerUseCase>()
    }
}
