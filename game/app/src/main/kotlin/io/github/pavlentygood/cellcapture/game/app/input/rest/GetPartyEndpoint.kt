package io.github.pavlentygood.cellcapture.game.app.input.rest

import io.github.pavlentygood.cellcapture.game.domain.*
import io.github.pavlentygood.cellcapture.game.restapi.CellResponse
import io.github.pavlentygood.cellcapture.game.restapi.DicesResponse
import io.github.pavlentygood.cellcapture.game.restapi.GetPartyApi
import io.github.pavlentygood.cellcapture.game.restapi.PartyResponse
import io.github.pavlentygood.cellcapture.game.restapi.PlayerResponse
import io.github.pavlentygood.cellcapture.game.app.usecase.GetPartyByPlayerUseCase
import io.github.pavlentygood.cellcapture.game.app.usecase.GetPartyUseCaseError
import io.github.pavlentygood.cellcapture.game.app.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GetPartyEndpoint(
    private val getParty: GetPartyByPlayerUseCase
) : GetPartyApi {

    override fun getParty(playerId: Int): ResponseEntity<PartyResponse> =
        getParty(PlayerId(playerId)).fold(
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
