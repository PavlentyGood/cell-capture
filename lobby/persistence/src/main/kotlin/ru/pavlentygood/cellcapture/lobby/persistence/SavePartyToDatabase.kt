package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PlayerDto
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@Transactional
class SavePartyToDatabase(
    private val partyRepository: PartyRepository
) : SaveParty {

    override fun invoke(party: Party) {
        val partyDto: PartyDto = party.toDto()
        partyRepository.save(partyDto)
    }
}

fun Party.toDto(): PartyDto {
    val party = PartyDto(
        id = id.toUUID(),
        started = started,
        ownerId = ownerId.toInt(),
        playerLimit = playerLimit.value,
        players = listOf()
    )
    party.players = getPlayers().map { it.toDto(party) }
    return party
}

fun Player.toDto(party: PartyDto) =
    PlayerDto(
        id = id.toInt(),
        name = name.toStringValue(),
        party = party
    )
