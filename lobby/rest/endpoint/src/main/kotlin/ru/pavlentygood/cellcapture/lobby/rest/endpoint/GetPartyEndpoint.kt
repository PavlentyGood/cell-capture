package ru.pavlentygood.cellcapture.lobby.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.rest.api.GetPartyApi
import ru.pavlentygood.cellcapture.lobby.rest.api.PartyResponse
import ru.pavlentygood.cellcapture.lobby.rest.api.PlayerResponse
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCase
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCaseError
import ru.pavlentygood.cellcapture.lobby.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import java.util.*

@RestController
class GetPartyEndpoint(
    private val getParty: GetPartyUseCase
) : GetPartyApi {

    override fun invoke(partyId: UUID): ResponseEntity<PartyResponse> =
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
