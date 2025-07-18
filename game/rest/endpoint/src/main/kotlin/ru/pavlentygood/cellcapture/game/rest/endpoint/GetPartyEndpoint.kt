package ru.pavlentygood.cellcapture.game.rest.endpoint

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.pavlentygood.cellcapture.game.domain.Cell
import ru.pavlentygood.cellcapture.game.domain.Dices
import ru.pavlentygood.cellcapture.game.domain.Field
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.rest.api.*
import ru.pavlentygood.cellcapture.game.usecase.GetPartyByPlayerUseCase
import ru.pavlentygood.cellcapture.game.usecase.GetPartyUseCaseError
import ru.pavlentygood.cellcapture.game.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

@RestController
class GetPartyEndpoint(
    private val getParty: GetPartyByPlayerUseCase
) : GetPartyApi {

    override fun invoke(playerId: Int): ResponseEntity<PartyResponse> =
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
