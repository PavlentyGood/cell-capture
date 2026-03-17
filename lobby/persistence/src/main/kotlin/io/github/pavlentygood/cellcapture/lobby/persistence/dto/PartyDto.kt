package io.github.pavlentygood.cellcapture.lobby.persistence.dto

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "parties")
class PartyDto(

    @Id
    @Column("id")
    val partyId: UUID,

    val version: Long,

    val started: Boolean,

    val ownerId: Int,

    val playerLimit: Int,

    @MappedCollection(idColumn = "party_id", keyColumn = "id")
    var players: List<PlayerDto>

) : Persistable<UUID> {

    override fun getId() = partyId
    override fun isNew() = version == 1L
}
