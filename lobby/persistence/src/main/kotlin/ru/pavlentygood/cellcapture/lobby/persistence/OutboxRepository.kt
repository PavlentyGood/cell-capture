package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jpa.repository.JpaRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxDto

interface OutboxRepository : JpaRepository<OutboxDto, Long>
