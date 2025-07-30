package ru.pavlentygood.cellcapture.lobby.rest.endpoint

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.rest.api.StartPartyApi
import ru.pavlentygood.cellcapture.lobby.usecase.StartPartyUseCase

@RestController
class StartPartyEndpoint(
    private val startParty: StartPartyUseCase
) : StartPartyApi {

    override fun invoke(playerId: Int): ResponseEntity<Unit> =
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
