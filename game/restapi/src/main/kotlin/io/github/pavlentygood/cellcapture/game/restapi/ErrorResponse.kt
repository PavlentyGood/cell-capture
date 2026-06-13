package io.github.pavlentygood.cellcapture.game.restapi

interface BaseErrorType

data class ErrorResponse(
    val type: BaseErrorType
)
