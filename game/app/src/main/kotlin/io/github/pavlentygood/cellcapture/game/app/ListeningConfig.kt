package io.github.pavlentygood.cellcapture.game.app

import io.github.pavlentygood.cellcapture.game.app.listening.ListenPartyStarted
import io.github.pavlentygood.cellcapture.game.app.listening.PartyStartedMessage
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
