package ru.pavlentygood.cellcapture.lobby.persistence.dto

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "parties")
class PartyDto(

    @Id
    val id: UUID,

    val started: Boolean,

    val ownerId: Int,

    val playerLimit: Int,

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL])
    var players: List<PlayerDto>
)
