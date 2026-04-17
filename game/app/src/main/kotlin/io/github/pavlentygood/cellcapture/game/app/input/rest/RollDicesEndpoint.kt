package io.github.pavlentygood.cellcapture.game.app.input.rest

import io.github.pavlentygood.cellcapture.game.domain.RolledDices
import io.github.pavlentygood.cellcapture.game.restapi.ErrorResponse
import io.github.pavlentygood.cellcapture.game.restapi.RestException
import io.github.pavlentygood.cellcapture.game.restapi.RollDicesApi
import io.github.pavlentygood.cellcapture.game.restapi.RollDicesApi.*
import io.github.pavlentygood.cellcapture.game.restapi.RollDicesApi.ErrorType.*
import io.github.pavlentygood.cellcapture.game.app.usecase.RollDicesUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.RollDicesUseCaseError
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class RollDicesEndpoint(
    private val roll: RollDicesUseCase
) : RollDicesApi {

    override fun rollDices(playerId: Int): ResponseEntity<RollResponse> =
        roll(PlayerId(playerId))
            .fold(
                { it.throwError() },
                { dices ->
                    val response = RollResponse(dices.toResponse())
                    ResponseEntity.ok(response)
                }
            )

    private fun RolledDices.toResponse() =
        DicesResponse(firstValue, secondValue)

    private fun RollDicesUseCaseError.throwError(): ResponseEntity<RollResponse> =
        when (this) {
            RollDicesUseCaseError.PlayerNotFound -> ResponseEntity.notFound().build()
            RollDicesUseCaseError.PlayerNotCurrent -> buildError(PLAYER_NOT_CURRENT)
            RollDicesUseCaseError.DicesAlreadyRolled -> buildError(DICES_ALREADY_ROLLED)
            RollDicesUseCaseError.PartyCompleted -> buildError(PARTY_COMPLETED)
        }

    private fun buildError(type: ErrorType): ResponseEntity<RollResponse> {
        val response = ResponseEntity.unprocessableEntity().body(ErrorResponse(type))
        throw RestException(response)
    }
}
