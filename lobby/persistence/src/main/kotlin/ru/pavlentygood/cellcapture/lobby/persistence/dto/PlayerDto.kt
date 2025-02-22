package ru.pavlentygood.cellcapture.lobby.persistence.dto

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "players")
class PlayerDto(

    @Id
    val id: Int,

    val name: String,

    @ManyToOne
    val party: PartyDto
)
