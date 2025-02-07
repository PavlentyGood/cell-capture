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
import ru.pavlentygood.cellcapture.game.domain.dices
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.playerId

@WebMvcTest
@ContextConfiguration(classes = [RollEndpointTest.Config::class, RollEndpoint::class])
internal class RollEndpointTest {

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
            row(RollUseCase.DicesAlreadyRolled),
            row(RollUseCase.PartyAlreadyCompleted)
        ).forAll { (useCaseError) ->
            val playerId = playerId()

            every { roll(playerId) } returns useCaseError.left()

            post(playerId).andExpect { status { isUnprocessableEntity() } }
        }
    }

    @Test
    fun `roll dices`() {
        val playerId = playerId()
        val dices = dices()

        every { roll(playerId) } returns dices.right()

        post(playerId).andExpect {
            status { isOk() }
            content {
                jsonPath("$.dices.first") { value(dices.first.value) }
                jsonPath("$.dices.second") { value(dices.second.value) }
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
