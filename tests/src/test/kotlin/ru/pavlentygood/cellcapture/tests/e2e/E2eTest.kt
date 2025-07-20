package ru.pavlentygood.cellcapture.tests.e2e

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.eventuallyConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import java.io.File
import java.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@EnableFeignClients
@SpringBootTest(classes = [
    CreatePartyClient::class,
    JoinPlayerClient::class,
    StartPartyClient::class,
    RollDicesClient::class,
    FeignAutoConfiguration::class,
    JacksonAutoConfiguration::class,
    HttpMessageConvertersAutoConfiguration::class
])
class E2eTest {

    @Autowired
    lateinit var createParty: CreatePartyClient
    @Autowired
    lateinit var joinPlayer: JoinPlayerClient
    @Autowired
    lateinit var startParty: StartPartyClient
    @Autowired
    lateinit var rollDices: RollDicesClient

    init {
        ComposeContainer(File("docker-compose.yml"))
            .waitingFor(
                "lobby",
                Wait.forLogMessage(".*Started LobbyApplicationKt.*", 1)
                    .withStartupTimeout(Duration.ofMinutes(5))
            )
            .withLogConsumer("kafka")
            .withLogConsumer("postgres-lobby")
            .withLogConsumer("postgres-game")
            .withLogConsumer("lobby")
            .withLogConsumer("game")
            .start()
    }

    @Test
    fun `play cell-capture project`() {
        val ownerName: PlayerName = playerName()
        val request = CreatePartyRequest(
            ownerName = ownerName.toStringValue()
        )
        val createdParty = createParty(request).body!!
        val ownerId = createdParty.ownerId

        val joinPlayerRequest = JoinPlayerRequest(
            name = playerName().toStringValue()
        )
        val playerId = joinPlayer(
            partyId = createdParty.id,
            request = joinPlayerRequest
        ).body!!.id

        startParty(ownerId)

        runBlocking {
            eventually(
                eventuallyConfig {
                    duration = 5.minutes
                    initialDelay = 5.seconds
                    interval = 5.seconds
                }
            ) {
                val dices = rollDices(ownerId).body!!
            }
        }
    }
}
