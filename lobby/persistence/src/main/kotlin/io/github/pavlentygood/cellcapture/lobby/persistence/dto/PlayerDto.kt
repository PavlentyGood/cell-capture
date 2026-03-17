package io.github.pavlentygood.cellcapture.lobby.persistence.dto

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "players")
class PlayerDto(

    @Id
    val id: Int,

    val name: String
)
