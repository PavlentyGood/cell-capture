package ru.pavlentygood.cellcapture.lobby.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerApi
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerRequest
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerResponse
import ru.pavlentygood.cellcapture.lobby.usecase.*
import java.util.*

@RestController
class JoinPlayerEndpoint(
    private val joinPlayer: JoinPlayerUseCase
) : JoinPlayerApi {

    override fun invoke(partyId: UUID, request: JoinPlayerRequest): ResponseEntity<*> =
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

fun JoinPlayerUseCaseError.toRestError(): ResponseEntity<Unit> =
    when (this) {
        PartyNotFoundUseCaseError -> ResponseEntity.notFound().build()
        AlreadyStartedUseCaseError -> ResponseEntity.unprocessableEntity().build()
        PlayerCountLimitUseCaseError -> ResponseEntity.unprocessableEntity().build()
    }
