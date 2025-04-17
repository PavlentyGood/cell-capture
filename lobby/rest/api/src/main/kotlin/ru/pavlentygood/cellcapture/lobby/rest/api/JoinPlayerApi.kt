package ru.pavlentygood.cellcapture.lobby.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

fun interface JoinPlayerApi {

    @PostMapping(API_V1_PARTIES_PLAYERS)
    fun invoke(
        @PathVariable partyId: UUID,
        @RequestBody request: JoinPlayerRequest
    ): ResponseEntity<*>
}

data class JoinPlayerRequest(
    val name: String
)

data class JoinPlayerResponse(
    val id: Int
)
