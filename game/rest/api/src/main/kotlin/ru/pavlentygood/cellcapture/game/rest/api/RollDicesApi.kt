package ru.pavlentygood.cellcapture.game.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

fun interface RollDicesApi {

    @PostMapping(API_V1_PLAYERS_DICES)
    operator fun invoke(@PathVariable playerId: Int): ResponseEntity<RollResponse>

    data class RollResponse(
        val dices: DicesResponse
    )

    data class DicesResponse(
        val first: Int,
        val second: Int
    )
}
