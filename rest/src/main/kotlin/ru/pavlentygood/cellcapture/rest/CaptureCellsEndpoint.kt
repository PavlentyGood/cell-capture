package ru.pavlentygood.cellcapture.rest

import arrow.core.flatMap
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.Area
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.Point
import ru.pavlentygood.cellcapture.usecase.CaptureCells

@RestController
class CaptureCellsEndpoint(
    private val captureCells: CaptureCells
) {
    @PostMapping(API_V1_PLAYERS_CELLS)
    fun invoke(
        @PathVariable playerId: Int,
        @RequestBody request: Request
    ): ResponseEntity<Unit> =
        request.toDomain().fold(
            { ResponseEntity.badRequest().build() },
            { area ->
                captureCells(PlayerId(playerId), area)
                    .fold(
                        { it.toRestError() },
                        { ResponseEntity.ok().build() }
                    )
            }
        )

    data class Request(
        val from: Point,
        val to: Point
    ) {
        data class Point(
            val x: Int,
            val y: Int
        )
    }

    private fun Request.toDomain() =
        from.toDomain().flatMap { fromPoint ->
            to.toDomain().map { toPoint ->
                Area(fromPoint, toPoint)
            }
        }

    private fun Request.Point.toDomain() =
        Point.from(x = x, y = y)

    fun CaptureCells.Error.toRestError(): ResponseEntity<Unit> =
        when (this) {
            CaptureCells.PlayerNotFound -> ResponseEntity.notFound().build()
            CaptureCells.PlayerNotCurrent,
            CaptureCells.DicesNotRolled,
            CaptureCells.InaccessibleArea,
            CaptureCells.MismatchedArea -> ResponseEntity.unprocessableEntity().build()
        }
}
