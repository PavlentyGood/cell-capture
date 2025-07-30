package ru.pavlentygood.cellcapture.lobby.rest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

fun interface CreatePartyApi {

    @PostMapping(API_V1_PARTIES)
    operator fun invoke(@RequestBody request: CreatePartyRequest): ResponseEntity<CreatePartyResponse>
}

data class CreatePartyRequest(
    val ownerName: String
)

data class CreatePartyResponse(
    val id: UUID,
    val ownerId: Int
)
