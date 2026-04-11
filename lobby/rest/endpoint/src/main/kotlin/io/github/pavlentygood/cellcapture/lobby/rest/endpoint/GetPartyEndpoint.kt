package io.github.pavlentygood.cellcapture.lobby.rest.endpoint

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.domain.Player
import io.github.pavlentygood.cellcapture.lobby.rest.api.GetPartyApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.PartyResponse
import io.github.pavlentygood.cellcapture.lobby.rest.api.PlayerResponse
import io.github.pavlentygood.cellcapture.lobby.app.usecase.GetPartyUseCase
import io.github.pavlentygood.cellcapture.lobby.app.usecase.GetPartyUseCaseError
import io.github.pavlentygood.cellcapture.lobby.app.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GetPartyEndpoint(
    private val getParty: GetPartyUseCase
) : GetPartyApi {

    override fun getParty(partyId: UUID): ResponseEntity<PartyResponse> =
        getParty(PartyId(partyId)).fold(
            { error -> error.toResponse() },
            { party -> ResponseEntity.ok(party.toResponse()) }
        )
}

fun Party.toResponse() =
    PartyResponse(
        id = id.toUUID(),
        started = started,
        ownerId = ownerId.toInt(),
        playerLimit = playerLimit.value,
        players = getPlayers().map { it.toResponse() }
    )

fun Player.toResponse() =
    PlayerResponse(
        id = id.toInt(),
        name = name.toStringValue()
    )

fun GetPartyUseCaseError.toResponse(): ResponseEntity<PartyResponse> =
    when (this) {
        PartyNotFoundUseCaseError -> ResponseEntity.notFound().build()
    }
