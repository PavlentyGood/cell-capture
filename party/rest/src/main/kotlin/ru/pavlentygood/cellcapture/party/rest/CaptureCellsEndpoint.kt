package ru.pavlentygood.cellcapture.party.rest

import arrow.core.flatMap
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.party.domain.Area
import ru.pavlentygood.cellcapture.party.domain.PlayerId
import ru.pavlentygood.cellcapture.party.domain.Point
import ru.pavlentygood.cellcapture.party.usecase.CaptureCellsUseCase

@RestController
class CaptureCellsEndpoint(
    private val captureCells: CaptureCellsUseCase
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
        val first: Point,
        val second: Point
    ) {
        data class Point(
            val x: Int,
            val y: Int
        )
    }

    private fun Request.toDomain() =
        first.toDomain().flatMap { firstPoint ->
            second.toDomain().map { secondPoint ->
                Area.from(firstPoint, secondPoint)
            }
        }

    private fun Request.Point.toDomain() =
        Point.from(x = x, y = y)

    fun CaptureCellsUseCase.Error.toRestError(): ResponseEntity<Unit> =
        when (this) {
            CaptureCellsUseCase.PlayerNotFound -> ResponseEntity.notFound().build()
            CaptureCellsUseCase.PlayerNotCurrent,
            CaptureCellsUseCase.DicesNotRolled,
            CaptureCellsUseCase.InaccessibleArea,
            CaptureCellsUseCase.MismatchedArea -> ResponseEntity.unprocessableEntity().build()
        }
}
