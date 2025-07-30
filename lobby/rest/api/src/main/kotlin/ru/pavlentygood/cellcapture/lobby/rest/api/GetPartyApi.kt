package ru.pavlentygood.cellcapture.lobby.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

fun interface GetPartyApi {

    @GetMapping(API_V1_PARTY_BY_ID)
    operator fun invoke(
        @PathVariable partyId: UUID
    ): ResponseEntity<PartyResponse>
}

data class PartyResponse(
    val id: UUID,
    val started: Boolean,
    val ownerId: Int,
    val playerLimit: Int,
    val players: List<PlayerResponse>
)

data class PlayerResponse(
    val id: Int,
    val name: String
)
