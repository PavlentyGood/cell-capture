package io.github.pavlentygood.cellcapture.game.rest.api

interface BaseErrorType

data class ErrorResponse(
    val type: BaseErrorType
)
