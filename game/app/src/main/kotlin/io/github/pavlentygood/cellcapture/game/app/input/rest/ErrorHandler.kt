package io.github.pavlentygood.cellcapture.game.app.input.rest

import io.github.pavlentygood.cellcapture.game.restapi.RestException
import io.github.pavlentygood.cellcapture.kernel.common.VersionConflictException
import io.github.pavlentygood.cellcapture.kernel.common.log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(VersionConflictException::class)
    fun versionConflict(e: VersionConflictException) {
        log.warn("Version conflict", e)
    }

    @ExceptionHandler(RestException::class)
    fun handleException(e: RestException) =
        e.responseEntity
}
