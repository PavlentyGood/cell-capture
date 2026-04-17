package io.github.pavlentygood.cellcapture.game.app.config

import io.github.pavlentygood.cellcapture.game.app.input.listening.ListenPartyStarted
import io.github.pavlentygood.cellcapture.game.app.input.listening.PartyStartedMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [ListenPartyStarted::class])
class ListeningConfig(
    private val listenPartyStarted: ListenPartyStarted
) {
    @Bean
    fun partyStarted() = { message: PartyStartedMessage ->
        listenPartyStarted(message)
    }
}