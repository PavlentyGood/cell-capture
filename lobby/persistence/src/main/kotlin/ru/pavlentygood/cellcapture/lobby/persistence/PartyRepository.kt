package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.pavlentygood.cellcapture.lobby.persistence.dto.PartyDto
import java.util.*

interface PartyRepository : CrudRepository<PartyDto, UUID> {

    @Query("""
        select p.* from players pl
        join parties p on p.id = pl.party_id
        where pl.id = :playerId
        """)
    fun getByPlayer(@Param("playerId") playerId: Int): Optional<PartyDto>

    @Query("""
        select version from parties where id = :partyId for update
    """)
    fun getVersion(@Param("partyId") partyId: UUID): Long?
}
