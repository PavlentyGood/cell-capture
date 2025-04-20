package ru.pavlentygood.cellcapture.game.rest.endpoint

import arrow.core.flatMap
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.Area
import ru.pavlentygood.cellcapture.game.domain.Point
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi.Request
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import ru.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCaseError
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@RestController
class CaptureCellsEndpoint(
    private val captureCells: CaptureCellsUseCase
) : CaptureCellsApi {

    override fun invoke(playerId: Int, request: Request): ResponseEntity<Unit> =
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

    private fun Request.toDomain() =
        first.toDomain().flatMap { firstPoint ->
            second.toDomain().map { secondPoint ->
                Area.from(firstPoint, secondPoint)
            }
        }

    private fun Request.Point.toDomain() =
        Point.from(x = x, y = y)

    fun CaptureCellsUseCaseError.toRestError(): ResponseEntity<Unit> =
        when (this) {
            CaptureCellsUseCaseError.PlayerNotFound -> ResponseEntity.notFound().build()
            CaptureCellsUseCaseError.PlayerNotCurrent,
            CaptureCellsUseCaseError.DicesNotRolled,
            CaptureCellsUseCaseError.InaccessibleArea,
            CaptureCellsUseCaseError.MismatchedArea,
            CaptureCellsUseCaseError.PartyCompleted -> ResponseEntity.unprocessableEntity().build()
        }
}
