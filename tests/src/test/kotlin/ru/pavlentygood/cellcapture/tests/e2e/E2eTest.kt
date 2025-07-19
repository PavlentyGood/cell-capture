package ru.pavlentygood.cellcapture.tests.e2e

import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.http.ResponseEntity
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.kernel.domain.playerName
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyResponse
import java.io.File
import java.time.Duration

@EnableFeignClients
@SpringBootTest(classes = [
    CreatePartyClient::class,
    FeignAutoConfiguration::class,
    JacksonAutoConfiguration::class,
    HttpMessageConvertersAutoConfiguration::class
])
class E2eTest {

    @Autowired
    lateinit var createParty: CreatePartyClient

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
    fun test() {
        val ownerName: PlayerName = playerName()
        val request = CreatePartyRequest(
            ownerName = ownerName.toStringValue()
        )

        val response: ResponseEntity<CreatePartyResponse> = createParty(request)

        response.body!!.ownerId shouldBeGreaterThan 0
    }
}
