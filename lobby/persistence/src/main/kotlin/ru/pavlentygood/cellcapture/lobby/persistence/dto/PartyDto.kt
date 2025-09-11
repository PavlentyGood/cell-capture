package ru.pavlentygood.cellcapture.lobby.persistence.dto

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "parties")
class PartyDto(

    @Id
    val id: UUID,

    val version: Long,

    val started: Boolean,

    val ownerId: Int,

    val playerLimit: Int,

    @OneToMany(mappedBy = "party", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var players: List<PlayerDto>
)
