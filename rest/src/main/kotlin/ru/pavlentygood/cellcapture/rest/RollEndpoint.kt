package ru.pavlentygood.cellcapture.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.DicePair
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.usecase.Roll

@RestController
class RollEndpoint(
    private val roll: Roll
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

    private fun Roll.Error.toError(): ResponseEntity<Unit> =
        when (this) {
            Roll.PlayerNotCurrent,
            Roll.DicesAlreadyRolled -> ResponseEntity.unprocessableEntity().build()
            Roll.PlayerNotFound -> ResponseEntity.notFound().build()
        }
}