package ru.pavlentygood.cellcapture.game.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

fun interface GetPartyApi {

    @GetMapping(API_V1_PLAYERS_PARTY)
    fun invoke(
        @PathVariable playerId: Int
    ): ResponseEntity<PartyResponse>
}

data class PartyResponse(
    val id: UUID,
    val completed: Boolean,
    val ownerId: Int,
    val currentPlayerId: Int,
    val dices: DicesResponse?,
    val players: List<PlayerResponse>,
    val cells: List<CellResponse>
)

data class DicesResponse(
    val first: Int,
    val second: Int
)

data class PlayerResponse(
    val id: Int,
    val name: String
)

data class CellResponse(
    val playerId: Int,
    val x: Int,
    val y: Int
)
