package ru.pavlentygood.cellcapture.game.rest

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
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.game.domain.PlayerId
import ru.pavlentygood.cellcapture.game.domain.dicePair
import ru.pavlentygood.cellcapture.game.domain.playerId
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase

@WebMvcTest
@ContextConfiguration(classes = [RollUseCaseEndpointTest.Config::class, RollEndpoint::class])
internal class RollUseCaseEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var roll: RollUseCase

    @Test
    fun `roll - not found`() {
        val playerId = playerId()

        every { roll(playerId) } returns RollUseCase.PlayerNotFound.left()

        post(playerId).andExpect { status { isNotFound() } }
    }

    @Test
    fun `roll - use case errors`() {
        listOf(
            row(RollUseCase.PlayerNotCurrent),
            row(RollUseCase.DicesAlreadyRolled)
        ).forAll { (useCaseError) ->
            val playerId = playerId()

            every { roll(playerId) } returns useCaseError.left()

            post(playerId).andExpect { status { isUnprocessableEntity() } }
        }
    }

    @Test
    fun `roll dices`() {
        val playerId = playerId()
        val dicePair = dicePair()

        every { roll(playerId) } returns dicePair.right()

        post(playerId).andExpect {
            status { isOk() }
            content {
                jsonPath("$.dicePair.first") { value(dicePair.first.value) }
                jsonPath("$.dicePair.second") { value(dicePair.second.value) }
            }
        }
    }

    private fun post(playerId: PlayerId) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_DICES.with("playerId", playerId.toInt())
        )

    @Configuration
    class Config {
        @Bean
        fun roll() = mockk<RollUseCase>()
    }
}
