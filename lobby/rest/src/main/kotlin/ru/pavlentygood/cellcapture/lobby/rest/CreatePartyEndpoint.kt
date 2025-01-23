package ru.pavlentygood.cellcapture.lobby.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.lobby.usecase.CreatePartyResult
import ru.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import java.net.URI
import java.util.*

@RestController
class CreatePartyEndpoint(
    private val createParty: CreatePartyUseCase
) {
    @PostMapping(API_V1_PARTIES)
    fun invoke(@RequestBody request: CreatePartyRequest): ResponseEntity<CreatePartyResponse> {
        return PlayerName.from(request.ownerName)
            .fold(
                { ResponseEntity.badRequest().build() },
                { ownerName ->
                    val response = createParty(ownerName).toResponse()
                    ResponseEntity
                        .created(URI.create("$API_V1_PARTIES/${response.id}"))
                        .body(response)
                }
            )
    }
}

data class CreatePartyRequest(
    val ownerName: String
)

data class CreatePartyResponse(
    val id: UUID,
    val ownerId: Int
)

fun CreatePartyResult.toResponse() =
    CreatePartyResponse(
        id = partyId.toUUID(),
        ownerId = ownerId.toInt()
    )
