package ru.pavlentygood.cellcapture.tests.e2e

import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

object Container {

    fun init() {
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
}
