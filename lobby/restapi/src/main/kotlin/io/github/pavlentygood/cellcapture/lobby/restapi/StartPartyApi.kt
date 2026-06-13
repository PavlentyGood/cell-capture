package io.github.pavlentygood.cellcapture.lobby.restapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

fun interface StartPartyApi {

    @PostMapping(API_V1_PARTIES_START)
    fun startParty(@RequestParam playerId: Int): ResponseEntity<Unit>
}
