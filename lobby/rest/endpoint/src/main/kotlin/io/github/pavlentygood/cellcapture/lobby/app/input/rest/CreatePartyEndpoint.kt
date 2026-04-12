package io.github.pavlentygood.cellcapture.lobby.app.input.rest

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.lobby.restapi.API_V1_PARTIES
import io.github.pavlentygood.cellcapture.lobby.restapi.CreatePartyApi
import io.github.pavlentygood.cellcapture.lobby.restapi.CreatePartyRequest
import io.github.pavlentygood.cellcapture.lobby.restapi.CreatePartyResponse
import io.github.pavlentygood.cellcapture.lobby.app.usecase.CreatePartyResult
import io.github.pavlentygood.cellcapture.lobby.app.usecase.CreatePartyUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreatePartyEndpoint(
    private val createParty: CreatePartyUseCase
) : CreatePartyApi {

    override fun createParty(request: CreatePartyRequest): ResponseEntity<CreatePartyResponse> {
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

fun CreatePartyResult.toResponse() =
    CreatePartyResponse(
        id = partyId.toUUID(),
        ownerId = ownerId.toInt()
    )
