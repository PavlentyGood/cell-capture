package io.github.pavlentygood.cellcapture.ai.app.integration

import io.github.pavlentygood.cellcapture.ai.app.integration.config.BaseKafkaTest
import io.github.pavlentygood.cellcapture.ai.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.ForestCreatedEventDto
import io.github.pavlentygood.cellcapture.ai.app.usecase.port.GetForest
import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestId
import io.github.pavlentygood.cellcapture.ai.domain.ForestName
import io.github.pavlentygood.cellcapture.ai.domain.forestId
import io.github.pavlentygood.cellcapture.ai.domain.forestName
import io.github.pavlentygood.cellcapture.ai.restapi.API_V1_FORESTS
import io.github.pavlentygood.cellcapture.ai.restapi.CreateForestRequest
import io.github.pavlentygood.cellcapture.kernel.common.mapper
import io.github.pavlentygood.cellcapture.kernel.domain.version
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@Import(value = [AiComponentTest.TestConsumerConfig::class])
@Sql(
    statements = ["truncate table outbox"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class AiComponentTest : BasePostgresTest, BaseKafkaTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var getForest: GetForest

    companion object {
        lateinit var latch: CountDownLatch
        var sentForestCreatedEvent: ForestCreatedEventDto? = null
    }

    @BeforeEach
    fun before() {
        latch = CountDownLatch(1)
        sentForestCreatedEvent = null
    }

    @RepeatedTest(2)
    fun `create forest as process`() {
        val id = forestId()
        val name = forestName()

        createForest(id, name)

        val forest: Forest = getForest(id)!!
        forest.id shouldBe id
        forest.name shouldBe name
        forest.version shouldBe version(1)

        latch.await(5, TimeUnit.SECONDS) shouldBe true
        sentForestCreatedEvent!!.id shouldBe id.toUUID()
        sentForestCreatedEvent!!.name shouldBe name.toText()
    }

    private fun createForest(forestId: ForestId, forestName: ForestName) =
        mockMvc.post(API_V1_FORESTS) {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(
                CreateForestRequest(id = forestId.toUUID(), name = forestName.toText())
            )
        }.andExpect { status { isNoContent() } }

    @TestConfiguration
    class TestConsumerConfig {
        @Bean
        fun forestCreated() = { message: ForestCreatedEventDto ->
            sentForestCreatedEvent = message
            latch.countDown()
        }
    }
}
