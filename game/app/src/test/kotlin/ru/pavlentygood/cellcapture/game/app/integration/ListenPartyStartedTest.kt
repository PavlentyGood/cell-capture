package ru.pavlentygood.cellcapture.game.app.integration

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.pavlentygood.cellcapture.game.app.integration.config.BaseKafkaTest
import ru.pavlentygood.cellcapture.game.app.partyStartedMessage
import ru.pavlentygood.cellcapture.game.persistence.BasePostgresTest
import ru.pavlentygood.cellcapture.game.rest.api.API_V1_PLAYERS_PARTY
import ru.pavlentygood.cellcapture.game.rest.endpoint.with
import kotlin.time.Duration.Companion.seconds

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = [TestDatabaseAutoConfiguration::class])
class ListenPartyStartedTest : BasePostgresTest, BaseKafkaTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var streamBridge: StreamBridge

    @Test
    fun `listen party started`() {
        val partyStarted = partyStartedMessage()

        streamBridge.send("partyStarted-out-0", partyStarted)

        runBlocking {
            eventually(
                eventuallyConfig {
                    duration = 5.seconds
                    initialDelay = 1.seconds
                }
            ) {
                mockMvc.get(API_V1_PLAYERS_PARTY.with("playerId", partyStarted.ownerId))
                    .andExpect {
                        status { isOk() }
                        content {
                            jsonPath("$.id") { value(partyStarted.partyId.toString()) }
                        }
                    }
            }
        }
    }
}
