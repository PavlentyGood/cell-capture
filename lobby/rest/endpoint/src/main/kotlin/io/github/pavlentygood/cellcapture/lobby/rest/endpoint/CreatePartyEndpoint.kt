package io.github.pavlentygood.cellcapture.lobby.rest.endpoint

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.lobby.rest.api.API_V1_PARTIES
import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyRequest
import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyResponse
import io.github.pavlentygood.cellcapture.lobby.usecase.CreatePartyResult
import io.github.pavlentygood.cellcapture.lobby.usecase.CreatePartyUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreatePartyEndpoint(
    private val createParty: CreatePartyUseCase
) : CreatePartyApi {

    override fun invoke(request: CreatePartyRequest): ResponseEntity<CreatePartyResponse> {
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
