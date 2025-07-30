package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jpa.repository.JpaRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyDto
import java.util.*

interface PartyRepository : JpaRepository<PartyDto, UUID> {

    fun getByPlayersId(playerId: Int): Optional<PartyDto>
}
