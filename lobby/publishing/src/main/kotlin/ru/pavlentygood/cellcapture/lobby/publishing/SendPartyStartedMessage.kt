package ru.pavlentygood.cellcapture.lobby.publishing

import org.springframework.kafka.core.KafkaTemplate
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.PublishPartyStartedEvent

const val PARTY_STARTED_TOPIC = "party-started"

class SendPartyStartedMessage(
    private val kafkaTemplate: KafkaTemplate<String, PartyDto>
) : PublishPartyStartedEvent {

    override fun invoke(party: Party) {
        kafkaTemplate.send(PARTY_STARTED_TOPIC, party.toDto())
    }
}

fun Party.toDto() =
    PartyDto(
        id = id.toUUID(),
        ownerId = ownerId.toInt(),
        players = getPlayers().map { it.toDto() }
    )

fun Player.toDto() =
    PlayerDto(
        id = id.toInt(),
        name = name.toStringValue()
    )
