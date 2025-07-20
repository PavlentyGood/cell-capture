package ru.pavlentygood.cellcapture.lobby.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

fun interface StartPartyApi {

    @PostMapping(API_V1_PARTIES_START)
    operator fun invoke(@RequestParam playerId: Int): ResponseEntity<Unit>
}
