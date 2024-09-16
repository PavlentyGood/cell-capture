package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import ru.pavlentygood.cellcapture.domain.Area
import ru.pavlentygood.cellcapture.domain.PlayerId

interface CaptureCells : (PlayerId, Area) -> Either<CaptureCells.Error, Unit> {

    sealed interface Error
    data object PlayerNotFound : Error
    data object PlayerNotCurrent : Error
    data object MismatchedArea : Error
    data object InaccessibleArea : Error
}
