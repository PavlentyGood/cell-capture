package ru.pavlentygood.cellcapture.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.usecase.StartParty

@RestController
class StartPartyEndpoint(
    private val startParty: StartParty
) {
    @PostMapping(API_V1_PARTIES_START)
    fun invoke(@RequestParam playerId: Int): ResponseEntity<Unit> =
        startParty(PlayerId(playerId))
            .fold(
                { it.toRestError() },
                { ResponseEntity.ok().build() }
            )
}

fun StartParty.Error.toRestError(): ResponseEntity<Unit> =
    when (this) {
        StartParty.PlayerNotFound -> ResponseEntity.notFound().build()
        StartParty.PlayerNotOwner -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        StartParty.TooFewPlayers,
        StartParty.AlreadyCompleted,
        StartParty.AlreadyStarted -> ResponseEntity.unprocessableEntity().build()
    }
