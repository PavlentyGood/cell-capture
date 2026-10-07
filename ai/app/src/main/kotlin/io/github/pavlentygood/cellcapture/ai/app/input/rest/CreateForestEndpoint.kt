package io.github.pavlentygood.cellcapture.ai.app.input.rest

import io.github.pavlentygood.cellcapture.ai.app.usecase.CreateForestUseCase
import io.github.pavlentygood.cellcapture.ai.app.usecase.CreateForestUseCaseError
import io.github.pavlentygood.cellcapture.ai.domain.ForestId
import io.github.pavlentygood.cellcapture.ai.domain.ForestName
import io.github.pavlentygood.cellcapture.ai.restapi.CreateForestApi
import io.github.pavlentygood.cellcapture.ai.restapi.CreateForestRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateForestEndpoint(
    private val createForest: CreateForestUseCase
) : CreateForestApi {

    override fun createForest(request: CreateForestRequest): ResponseEntity<Unit> {
        return ForestName.of(request.name)
            .fold(
                { ResponseEntity.badRequest().build() },
                { name ->
                    createForest(ForestId(request.id), name)
                        .fold(
                            { it.toRestError() },
                            { ResponseEntity.noContent().build() }
                        )
                }
            )
    }
}

fun CreateForestUseCaseError.toRestError(): ResponseEntity<Unit> =
    when (this) {
        CreateForestUseCaseError.ForestAlreadyExistsUseCaseError -> ResponseEntity.unprocessableEntity().build()
    }
