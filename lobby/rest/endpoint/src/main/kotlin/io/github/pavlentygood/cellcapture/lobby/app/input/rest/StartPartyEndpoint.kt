package io.github.pavlentygood.cellcapture.lobby.app.input.rest

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.restapi.StartPartyApi
import io.github.pavlentygood.cellcapture.lobby.app.usecase.StartPartyUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StartPartyEndpoint(
    private val startParty: StartPartyUseCase
) : StartPartyApi {

    override fun startParty(playerId: Int): ResponseEntity<Unit> =
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
        StartPartyUseCase.AlreadyStarted -> ResponseEntity.unprocessableEntity().build()
    }
