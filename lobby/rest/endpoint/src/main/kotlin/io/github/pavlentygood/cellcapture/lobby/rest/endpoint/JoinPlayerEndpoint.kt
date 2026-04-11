package io.github.pavlentygood.cellcapture.lobby.rest.endpoint

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.lobby.restapi.JoinPlayerApi
import io.github.pavlentygood.cellcapture.lobby.restapi.JoinPlayerRequest
import io.github.pavlentygood.cellcapture.lobby.restapi.JoinPlayerResponse
import io.github.pavlentygood.cellcapture.lobby.app.usecase.JoinPlayerUseCase
import io.github.pavlentygood.cellcapture.lobby.app.usecase.JoinPlayerUseCaseError
import io.github.pavlentygood.cellcapture.lobby.app.usecase.JoinPlayerUseCaseError.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class JoinPlayerEndpoint(
    private val joinPlayer: JoinPlayerUseCase
) : JoinPlayerApi {

    override fun joinPlayer(
        partyId: UUID,
        request: JoinPlayerRequest
    ): ResponseEntity<JoinPlayerResponse> =
        PlayerName.from(request.name).fold(
            { ResponseEntity.badRequest().build() },
            { name ->
                joinPlayer(PartyId(partyId), name).fold(
                    { it.toRestError() },
                    { playerId -> ResponseEntity.ok(JoinPlayerResponse(id = playerId.toInt())) }
                )
            }
        )
}

fun JoinPlayerUseCaseError.toRestError(): ResponseEntity<JoinPlayerResponse> =
    when (this) {
        PartyNotFoundUseCaseError -> ResponseEntity.notFound().build()
        AlreadyStartedUseCaseError -> ResponseEntity.unprocessableEntity().build()
        PlayerCountLimitUseCaseError -> ResponseEntity.unprocessableEntity().build()
    }
