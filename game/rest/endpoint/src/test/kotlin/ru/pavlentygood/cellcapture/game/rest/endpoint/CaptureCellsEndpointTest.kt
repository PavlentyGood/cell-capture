package ru.pavlentygood.cellcapture.game.rest.endpoint

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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.game.domain.area
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_CELLS
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCaseError
import ru.pavlentygood.cellcapture.kernel.domain.playerId

@WebMvcTest
@ContextConfiguration(classes = [CaptureCellsEndpointTest.Config::class, CaptureCellsEndpoint::class])
class CaptureCellsEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var captureCells: CaptureCellsUseCase

    private val area = area()

    @Test
    fun `capture cells`() {
        val playerId = playerId()

        every { captureCells(playerId, area) } returns Unit.right()

        post(playerId.toInt()).andExpect { status { isOk() } }
    }

    @Test
    fun `capture cells - player not found`() {
        val playerId = playerId()

        every { captureCells(playerId, area) } returns CaptureCellsUseCaseError.PlayerNotFound.left()

        post(playerId.toInt()).andExpect { status { isNotFound() } }
    }

    @Test
    fun `capture cells - player not current`() {
        val playerId = playerId()

        every { captureCells(playerId, area) } returns CaptureCellsUseCaseError.PlayerNotCurrent.left()

        post(playerId.toInt()).andExpect { status { isUnprocessableEntity() } }
    }

    private fun post(playerId: Any) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_CELLS.with("playerId", playerId)
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(
                CaptureCellsApi.Request(
                    first = CaptureCellsApi.Request.Point(area.from.x, area.from.y),
                    second = CaptureCellsApi.Request.Point(area.to.x, area.to.y)
                )
            )
        }

    @Configuration
    class Config {
        @Bean
        fun captureCells() = mockk<CaptureCellsUseCase>()
    }
}
