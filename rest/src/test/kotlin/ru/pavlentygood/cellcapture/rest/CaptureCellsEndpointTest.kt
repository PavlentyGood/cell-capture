package ru.pavlentygood.cellcapture.rest

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
import ru.pavlentygood.cellcapture.domain.area
import ru.pavlentygood.cellcapture.domain.playerId
import ru.pavlentygood.cellcapture.usecase.CaptureCells

@WebMvcTest
@ContextConfiguration(classes = [CaptureCellsEndpointTest.Config::class, CaptureCellsEndpoint::class])
class CaptureCellsEndpointTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var captureCells: CaptureCells

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

        every { captureCells(playerId, area) } returns CaptureCells.PlayerNotFound.left()

        post(playerId.toInt()).andExpect { status { isNotFound() } }
    }

    @Test
    fun `capture cells - player not current`() {
        val playerId = playerId()

        every { captureCells(playerId, area) } returns CaptureCells.PlayerNotCurrent.left()

        post(playerId.toInt()).andExpect { status { isUnprocessableEntity() } }
    }

    private fun post(playerId: Any) =
        mockMvc.post(
            urlTemplate = API_V1_PLAYERS_CELLS.with("playerId", playerId)
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(
                CaptureCellsEndpoint.Request(
                    from = CaptureCellsEndpoint.Request.Cell(area.from.x, area.from.y),
                    to = CaptureCellsEndpoint.Request.Cell(area.to.x, area.to.y)
                )
            )
        }

    @Configuration
    class Config {
        @Bean
        fun captureCells() = mockk<CaptureCells>()
    }
}
