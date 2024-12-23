package ru.pavlentygood.cellcapture.rest

import org.springframework.http.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerName
import ru.pavlentygood.cellcapture.usecase.JoinPlayerUseCase
import ru.pavlentygood.cellcapture.usecase.JoinPlayerError
import ru.pavlentygood.cellcapture.usecase.PlayerCountLimitUseCaseError
import ru.pavlentygood.cellcapture.usecase.PartyNotFoundUseCaseError
import java.util.*

@RestController
class JoinPlayerEndpoint(
    private val joinPlayer: JoinPlayerUseCase
) {
    @PostMapping(API_V1_PARTIES_PLAYERS)
    fun invoke(
        @PathVariable partyId: UUID,
        @RequestBody request: JoinPlayerRequest
    ): ResponseEntity<*> =
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

data class JoinPlayerRequest(
    val name: String
)

data class JoinPlayerResponse(
    val id: Int
)

fun JoinPlayerError.toRestError(): ResponseEntity<Unit> =
    when (this) {
        PartyNotFoundUseCaseError -> ResponseEntity.notFound().build()
        PlayerCountLimitUseCaseError -> ResponseEntity.unprocessableEntity().build()
    }
