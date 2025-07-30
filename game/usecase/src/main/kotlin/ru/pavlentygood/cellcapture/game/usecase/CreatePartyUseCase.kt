package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.PartyInfo
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

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
