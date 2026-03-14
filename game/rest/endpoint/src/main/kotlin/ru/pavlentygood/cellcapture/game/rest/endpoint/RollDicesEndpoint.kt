package ru.pavlentygood.cellcapture.game.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.RolledDices
import ru.pavlentygood.cellcapture.game.rest.api.ErrorResponse
import ru.pavlentygood.cellcapture.game.rest.api.RestException
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.*
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.ErrorType.*
import ru.pavlentygood.cellcapture.game.usecase.RollDicesUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollDicesUseCaseError
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@RestController
class RollDicesEndpoint(
    private val roll: RollDicesUseCase
) : RollDicesApi {

    override fun invoke(playerId: Int): ResponseEntity<RollResponse> =
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
