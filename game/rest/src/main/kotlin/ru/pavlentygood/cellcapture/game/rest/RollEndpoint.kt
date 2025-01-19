package ru.pavlentygood.cellcapture.game.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.DicePair
import ru.pavlentygood.cellcapture.game.domain.PlayerId
import ru.pavlentygood.cellcapture.game.usecase.RollUseCase

@RestController
class RollEndpoint(
    private val roll: RollUseCase
) {
    @PostMapping(API_V1_PLAYERS_DICES)
    fun invoke(@PathVariable playerId: Int): Any? =
        roll(PlayerId(playerId))
            .fold(
                { it.toError() },
                { dicePair ->
                    val response = RollResponse(dicePair.toResponse())
                    ResponseEntity.ok(response)
                }
            )

    data class RollResponse(
        val dicePair: DicePairResponse
    )

    data class DicePairResponse(
        val first: Int,
        val second: Int
    )

    private fun DicePair.toResponse() =
        DicePairResponse(first.value, second.value)

    private fun RollUseCase.Error.toError(): ResponseEntity<Unit> =
        when (this) {
            RollUseCase.PlayerNotCurrent,
            RollUseCase.DicesAlreadyRolled -> ResponseEntity.unprocessableEntity().build()
            RollUseCase.PlayerNotFound -> ResponseEntity.notFound().build()
        }
}
