package io.github.pavlentygood.cellcapture.game.rest.endpoint

import io.github.pavlentygood.cellcapture.game.domain.*
import io.github.pavlentygood.cellcapture.game.rest.api.*
import io.github.pavlentygood.cellcapture.game.usecase.GetPartyByPlayerUseCase
import io.github.pavlentygood.cellcapture.game.usecase.GetPartyUseCaseError
import io.github.pavlentygood.cellcapture.game.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GetPartyEndpoint(
    private val getParty: GetPartyByPlayerUseCase
) : GetPartyApi {

    override fun invoke(request: GetPartyRequest): ResponseEntity<PartyResponse> =
        getParty(PlayerId(request.playerId)).fold(
            { error -> error.toResponse() },
            { party -> ResponseEntity.ok(party.toResponse()) }
        )
}

fun Party.toResponse() =
    PartyResponse(
        id = id.toUUID(),
        completed = completed,
        ownerId = ownerId.toInt(),
        currentPlayerId = currentPlayerId.toInt(),
        dices = dices.toResponse(),
        players = players.map { it.toResponse() },
        cells = cells.flatten()
            .filter { it.playerId != Field.nonePlayerId }
            .map { it.toResponse() }
    )

fun Dices.toResponse() =
    if (rolled) {
        DicesResponse(
            first = firstValue!!,
            second = secondValue!!
        )
    } else {
        null
    }

fun Player.toResponse() =
    PlayerResponse(
        id = id.toInt(),
        name = name.toStringValue()
    )

fun Cell.toResponse() =
    CellResponse(
        playerId = playerId.toInt(),
        x = x,
        y = y
    )

fun GetPartyUseCaseError.toResponse(): ResponseEntity<PartyResponse> =
    when (this) {
        PartyNotFoundUseCaseError -> ResponseEntity.notFound().build()
    }
