package ru.pavlentygood.cellcapture.game.listening

import arrow.core.getOrElse
import org.springframework.kafka.annotation.KafkaListener
import ru.pavlentygood.cellcapture.game.domain.PartyInfo
import ru.pavlentygood.cellcapture.game.domain.PlayerList
import ru.pavlentygood.cellcapture.game.usecase.CreatePartyUseCase
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName

const val PARTY_STARTED_TOPIC = "party-started"

class PartyStartedListener(
    private val createParty: CreatePartyUseCase
) {
    @KafkaListener(topics = [PARTY_STARTED_TOPIC], groupId = "game")
    operator fun invoke(message: PartyStartedMessage) {
        val partyInfo: PartyInfo = message.toDomain()
        createParty(partyInfo)
    }
}

fun PartyStartedMessage.toDomain() =
    PartyInfo(
        partyId = PartyId(partyId),
        playerList = PlayerList.from(
            ownerId = PlayerId(ownerId),
            players = players.map { it.toDomain() }
        ).getOrElse {
            error("Illegal player list: $it")
        }
    )

fun PartyStartedMessage.Player.toDomain() =
    Player(
        id = PlayerId(id),
        name = PlayerName.from(name).getOrElse {
            error("Illegal player name ($name): $it")
        }
    )
