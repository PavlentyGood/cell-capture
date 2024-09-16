package ru.pavlentygood.cellcapture.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.Area
import ru.pavlentygood.cellcapture.domain.Cell
import ru.pavlentygood.cellcapture.domain.PlayerId
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
        captureCells(PlayerId(playerId), request.toDomain())
            .fold(
                { it.toRestError() },
                { ResponseEntity.ok().build() }
            )

    data class Request(
        val from: Cell,
        val to: Cell
    ) {
        data class Cell(
            val x: Int,
            val y: Int
        )
    }

    private fun Request.toDomain() =
        Area(
            from = from.toDomain(),
            to = to.toDomain()
        )

    private fun Request.Cell.toDomain() =
        Cell(x = x, y = y)

    fun CaptureCells.Error.toRestError(): ResponseEntity<Unit> =
        when (this) {
            CaptureCells.PlayerNotFound -> ResponseEntity.notFound().build()
            CaptureCells.PlayerNotCurrent,
            CaptureCells.InaccessibleArea,
            CaptureCells.MismatchedArea -> ResponseEntity.unprocessableEntity().build()
        }
}
