package ru.pavlentygood.cellcapture.tests.e2e

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

@FeignClient(
    name = "lobby-client",
    url = "http://localhost:8080",
    path = "/api/v1/parties"
)
interface LobbyClient {

    @PostMapping
    fun createParty(request: CreatePartyRequest): CreatePartyResponse
}

data class CreatePartyRequest(
    val ownerName: String
)

data class CreatePartyResponse(
    val id: UUID,
    val ownerId: Int
)
