package io.github.pavlentygood.cellcapture.game.rest.endpoint

import arrow.core.flatMap
import io.github.pavlentygood.cellcapture.game.domain.Area
import io.github.pavlentygood.cellcapture.game.domain.Point
import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi.ErrorType
import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi.ErrorType.*
import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi.Request
import io.github.pavlentygood.cellcapture.game.rest.api.ErrorResponse
import io.github.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCase
import io.github.pavlentygood.cellcapture.game.usecase.CaptureCellsUseCaseError
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CaptureCellsEndpoint(
    private val captureCells: CaptureCellsUseCase
) : CaptureCellsApi {

    override fun invoke(playerId: Int, request: Request): ResponseEntity<Any> =
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

    private fun CaptureCellsUseCaseError.toRestError(): ResponseEntity<Any> =
        when (this) {
            CaptureCellsUseCaseError.PlayerNotFound -> ResponseEntity.notFound().build()
            CaptureCellsUseCaseError.PlayerNotCurrent -> buildError(PLAYER_NOT_CURRENT)
            CaptureCellsUseCaseError.DicesNotRolled -> buildError(DICES_NOT_ROLLED)
            CaptureCellsUseCaseError.InaccessibleArea -> buildError(INACCESSIBLE_AREA)
            CaptureCellsUseCaseError.MismatchedArea -> buildError(MISMATCHED_AREA)
            CaptureCellsUseCaseError.PartyCompleted -> buildError(PARTY_COMPLETED)
        }

    private fun buildError(type: ErrorType): ResponseEntity<Any> =
        ResponseEntity.unprocessableEntity().body(ErrorResponse(type))
}
