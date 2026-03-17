package io.github.pavlentygood.cellcapture.lobby.rest.endpoint

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerResponse
import io.github.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCase
import io.github.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCaseError
import io.github.pavlentygood.cellcapture.lobby.usecase.JoinPlayerUseCaseError.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class JoinPlayerEndpoint(
    private val joinPlayer: JoinPlayerUseCase
) : JoinPlayerApi {

    override fun invoke(
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
