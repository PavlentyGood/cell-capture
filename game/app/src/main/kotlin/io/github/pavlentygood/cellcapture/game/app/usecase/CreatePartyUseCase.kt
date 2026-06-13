package io.github.pavlentygood.cellcapture.game.app.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.pavlentygood.cellcapture.game.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.game.domain.CreateParty
import io.github.pavlentygood.cellcapture.game.domain.Party
import io.github.pavlentygood.cellcapture.game.domain.PartyInfo

class CreatePartyUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val createParty: CreateParty,
    private val saveParty: SaveParty
) {
    operator fun invoke(partyInfo: PartyInfo): Either<PartyAlreadyExists, Unit> {
        val party: Party? = getPartyByPlayer(partyInfo.ownerId)
        return if (party != null) {
            PartyAlreadyExists.left()
        } else {
            val created: Party = createParty(partyInfo)
            saveParty(created).right()
        }
    }
}

data object PartyAlreadyExists
