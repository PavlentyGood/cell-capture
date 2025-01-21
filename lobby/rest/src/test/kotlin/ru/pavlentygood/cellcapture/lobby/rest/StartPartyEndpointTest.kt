package ru.pavlentygood.cellcapture.lobby.rest

import arrow.core.left
import arrow.core.right
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.lobby.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.playerId
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
    fun `start party - any client error`() {
        val playerId = playerId()

        every { startParty(playerId) } returns StartPartyUseCase.PlayerNotFound.left()

        post(playerId).andExpect {
            status { isNotFound() }
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
