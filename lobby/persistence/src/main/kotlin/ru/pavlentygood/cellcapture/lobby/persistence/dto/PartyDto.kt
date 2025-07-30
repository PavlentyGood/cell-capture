package ru.pavlentygood.cellcapture.lobby.persistence.dto

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "parties")
class PartyDto(

    @Id
    val id: UUID,

    val started: Boolean,

    val ownerId: Int,

    val playerLimit: Int,

    @OneToMany(mappedBy = "party", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var players: List<PlayerDto>
)
