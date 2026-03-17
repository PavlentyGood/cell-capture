package io.github.pavlentygood.cellcapture.lobby.persistence

import io.github.pavlentygood.cellcapture.lobby.persistence.dto.OutboxDto
import io.github.pavlentygood.cellcapture.lobby.persistence.dto.OutboxReadDto
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

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
