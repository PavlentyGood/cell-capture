package ru.pavlentygood.cellcapture.game.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.RolledDices
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi.DicesResponse
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase
import ru.pavlentygood.cellcapture.game.usecase.RollUseCaseError
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@RestController
class RollDicesEndpoint(
    private val roll: RollUseCase
) : RollDicesApi {

    override fun invoke(playerId: Int): Any? =
        roll(PlayerId(playerId))
            .fold(
                { it.toError() },
                { dices ->
                    val response = RollDicesApi.RollResponse(dices.toResponse())
                    ResponseEntity.ok(response)
                }
            )

    private fun RolledDices.toResponse() =
        DicesResponse(firstValue, secondValue)

    private fun RollUseCaseError.toError(): ResponseEntity<Unit> =
        when (this) {
            RollUseCaseError.PlayerNotFound -> ResponseEntity.notFound().build()
            RollUseCaseError.PlayerNotCurrent,
            RollUseCaseError.DicesAlreadyRolled,
            RollUseCaseError.PartyCompleted -> ResponseEntity.unprocessableEntity().build()
        }
}
