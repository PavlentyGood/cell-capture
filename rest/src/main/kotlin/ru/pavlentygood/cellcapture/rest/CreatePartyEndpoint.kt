package ru.pavlentygood.cellcapture.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.usecase.CreateParty
import java.net.URI
import java.util.UUID

@RestController
class CreatePartyEndpoint(
    private val createParty: CreateParty
) {
    @PostMapping(API_V1_PARTIES)
    fun invoke(): ResponseEntity<CreatePartyResponse> {
        val id: UUID = createParty().toUUID()
        return ResponseEntity
            .created(URI.create("$API_V1_PARTIES/$id"))
            .body(CreatePartyResponse(id = id))
    }
}

data class CreatePartyResponse(
    val id: UUID
)
