package io.github.pavlentygood.cellcapture.ai.app.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.pavlentygood.cellcapture.ai.app.input.rest.CreateForestEndpoint
import io.github.pavlentygood.cellcapture.ai.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.ai.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.ai.app.usecase.port.GetForest
import io.github.pavlentygood.cellcapture.ai.domain.forestId
import io.github.pavlentygood.cellcapture.ai.domain.forestName
import io.github.pavlentygood.cellcapture.ai.restapi.API_V1_FORESTS
import io.github.pavlentygood.cellcapture.ai.restapi.CreateForestRequest
import io.github.pavlentygood.cellcapture.kernel.domain.version
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(classes = [CreateForestEndpoint::class])
@Import(IntegrationConfig::class)
class CreateForestEndpointTest : BasePostgresTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    lateinit var getForest: GetForest

    @Test
    fun `create forest`() {
        val id = forestId()
        val name = forestName()

        val result = createForest(id.toUUID(), name.toText())

        result.andExpect {
            status { isNoContent() }
        }

        val storedForest = getForest(id)!!
        storedForest.id shouldBe id
        storedForest.name shouldBe name
        storedForest.version shouldBe version(1)
    }

    @Test
    fun `duplicate forest id`() {
        val id = forestId()

        createForest(id.toUUID(), "Taiga")
        createForest(id.toUUID(), "Taiga").andExpect {
            status { isUnprocessableEntity() }
        }
    }

    @Test
    fun `illegal forest name`() {
        createForest(forestId().toUUID(), "").andExpect {
            status { isBadRequest() }
        }
        createForest(forestId().toUUID(), " ").andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `forest name is too long`() {
        createForest(forestId().toUUID(), "x".repeat(51)).andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `illegal forest id`() {
        mockMvc.post(API_V1_FORESTS) {
            contentType = MediaType.APPLICATION_JSON
            content = """{"id":"not-a-uuid","name":"Taiga"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    private fun createForest(id: UUID, name: String) =
        mockMvc.post(API_V1_FORESTS) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(CreateForestRequest(id = id, name = name))
        }
}
