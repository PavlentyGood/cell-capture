package ru.pavlentygood.cellcapture.game.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.pavlentygood.cellcapture.game.app.listening.ListenPartyStarted
import ru.pavlentygood.cellcapture.game.app.listening.PartyStartedMessage

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
