package io.github.pavlentygood.cellcapture.ai.app.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.pavlentygood.cellcapture.ai.app.usecase.port.GetForest
import io.github.pavlentygood.cellcapture.ai.app.usecase.port.SaveForest
import io.github.pavlentygood.cellcapture.ai.domain.Forest
import io.github.pavlentygood.cellcapture.ai.domain.ForestId
import io.github.pavlentygood.cellcapture.ai.domain.ForestName

class CreateForestUseCase(
    private val getForest: GetForest,
    private val saveForest: SaveForest
) {
    operator fun invoke(id: ForestId, name: ForestName): Either<CreateForestUseCaseError, Unit> =
        if (getForest(id) == null) {
            saveForest(Forest.create(id, name))
            Unit.right()
        } else {
            CreateForestUseCaseError.ForestAlreadyExistsUseCaseError.left()
        }
}

sealed interface CreateForestUseCaseError {
    data object ForestAlreadyExistsUseCaseError : CreateForestUseCaseError
}
