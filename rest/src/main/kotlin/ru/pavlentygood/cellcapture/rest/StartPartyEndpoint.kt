package ru.pavlentygood.cellcapture.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.usecase.StartPartyUseCase

@RestController
class StartPartyEndpoint(
    private val startParty: StartPartyUseCase
) {
    @PostMapping(API_V1_PARTIES_START)
    fun invoke(@RequestParam playerId: Int): ResponseEntity<Unit> =
        startParty(PlayerId(playerId))
            .fold(
                { it.toRestError() },
                { ResponseEntity.ok().build() }
            )
}

fun StartPartyUseCase.Error.toRestError(): ResponseEntity<Unit> =
    when (this) {
        StartPartyUseCase.PlayerNotFound -> ResponseEntity.notFound().build()
        StartPartyUseCase.PlayerNotOwner -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        StartPartyUseCase.TooFewPlayers,
        StartPartyUseCase.AlreadyCompleted,
        StartPartyUseCase.AlreadyStarted -> ResponseEntity.unprocessableEntity().build()
    }
