package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jpa.repository.JpaRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyDto
import java.util.UUID

interface PartyRepository : JpaRepository<PartyDto, UUID>
