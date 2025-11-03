package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxDto
import ru.pavlentygood.cellcapture.lobby.persistence.dto.OutboxReadDto

interface OutboxRepository : CrudRepository<OutboxDto, Long> {

    @Query(
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
        value = """
            update outbox
            set status = 'SENT', updated = current_timestamp
            where id = :id
        """
    )
    @Modifying
    fun markAsSent(id: Long)
}
