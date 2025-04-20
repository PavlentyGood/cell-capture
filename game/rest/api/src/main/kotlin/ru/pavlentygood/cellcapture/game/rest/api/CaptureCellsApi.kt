package ru.pavlentygood.cellcapture.game.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

fun interface CaptureCellsApi {

    @PostMapping(API_V1_PLAYERS_CELLS)
    fun invoke(
        @PathVariable playerId: Int,
        @RequestBody request: Request
    ): ResponseEntity<Unit>

    data class Request(
        val first: Point,
        val second: Point
    ) {
        data class Point(
            val x: Int,
            val y: Int
        )
    }
}
