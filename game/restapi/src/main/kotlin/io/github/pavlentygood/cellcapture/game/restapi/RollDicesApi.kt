package io.github.pavlentygood.cellcapture.game.restapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

fun interface RollDicesApi {

    @PostMapping(API_V1_PLAYERS_DICES)
    fun rollDices(@PathVariable playerId: Int): ResponseEntity<RollResponse>

    data class RollResponse(
        val dices: DicesResponse
    )

    data class DicesResponse(
        val first: Int,
        val second: Int
    )

    enum class ErrorType : BaseErrorType {
        PLAYER_NOT_CURRENT,
        DICES_ALREADY_ROLLED,
        PARTY_COMPLETED
    }
}
