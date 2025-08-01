package ru.pavlentygood.cellcapture.lobby.rest.endpoint

import arrow.core.left
import arrow.core.right
import io.kotest.data.row
import io.kotest.inspectors.forAll
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.*
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES_START
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase

@WebMvcTest
@ContextConfiguration(classes = [StartPartyEndpointTest.Config::class, StartPartyEndpoint::class])
class StartPartyEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var startParty: StartPartyUseCase

    @Test
    fun `start party`() {
        val playerId = playerId()

        every { startParty(playerId) } returns Unit.right()

        post(playerId).andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `start party - use case errors`() {
        listOf(
            row(StartPartyUseCase.PlayerNotFound, NOT_FOUND),
            row(StartPartyUseCase.PlayerNotOwner, FORBIDDEN),
            row(StartPartyUseCase.TooFewPlayers, UNPROCESSABLE_ENTITY),
            row(StartPartyUseCase.AlreadyStarted, UNPROCESSABLE_ENTITY)
        ).forAll { (useCaseError, status) ->
            val playerId = playerId()

            every { startParty(playerId) } returns useCaseError.left()

            post(playerId).andExpect {
                status { isEqualTo(status.value()) }
            }
        }
    }

    private fun post(playerId: PlayerId) =
        mockMvc.post(API_V1_PARTIES_START) {
            queryParam("playerId", playerId.toInt().toString())
        }

    @Configuration
    class Config {
        @Bean
        fun startParty() = mockk<StartPartyUseCase>()
    }
}
