package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import ru.pavlentygood.cellcapture.domain.PlayerId

fun interface StartParty {
    operator fun invoke(playerId: PlayerId): Either<Error, Unit>

    sealed class Error
    data object PlayerNotFound : Error()
    data object PlayerNotOwner : Error()
    data object TooFewPlayers : Error()
    data object AlreadyStarted : Error()
    data object AlreadyCompleted : Error()
}
