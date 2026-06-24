package io.github.pavlentygood.cellcapture.ai.restapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

fun interface CreateForestApi {

    @PostMapping(API_V1_FORESTS)
    fun createForest(
        @RequestBody request: CreateForestRequest
    ): ResponseEntity<Unit>
}

data class CreateForestRequest(
    val id: UUID,
    val name: String
)
