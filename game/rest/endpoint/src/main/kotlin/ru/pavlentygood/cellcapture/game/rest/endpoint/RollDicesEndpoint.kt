package ru.pavlentygood.cellcapture.game.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.RolledDices
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.DicesResponse
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.RollResponse
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
                { it.toError() },
                { dices ->
                    val response = RollResponse(dices.toResponse())
                    ResponseEntity.ok(response)
                }
            )

    private fun RolledDices.toResponse() =
        DicesResponse(firstValue, secondValue)

    private fun RollDicesUseCaseError.toError(): ResponseEntity<RollResponse> =
        when (this) {
            RollDicesUseCaseError.PlayerNotFound -> ResponseEntity.notFound().build()
            RollDicesUseCaseError.PlayerNotCurrent,
            RollDicesUseCaseError.DicesAlreadyRolled,
            RollDicesUseCaseError.PartyCompleted -> ResponseEntity.unprocessableEntity().build()
        }
}
