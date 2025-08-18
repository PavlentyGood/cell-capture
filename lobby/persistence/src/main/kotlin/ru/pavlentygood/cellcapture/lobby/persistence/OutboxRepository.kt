package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxReadDto

interface OutboxRepository : JpaRepository<OutboxDto, Long> {

    @Query(
        nativeQuery = true,
        value = """
            select id, event_type, body
            from outbox
            where status = 'PENDING'
            order by created
            limit 1
            for update skip locked
        """
    )
    fun getNextRecord(): OutboxReadDto?

    @Query(
        nativeQuery = true,
        value = """
            update outbox
            set status = 'SENT', updated = current_timestamp
            where id = :id
        """
    )
    @Modifying
    fun markAsSent(id: Long)
}
