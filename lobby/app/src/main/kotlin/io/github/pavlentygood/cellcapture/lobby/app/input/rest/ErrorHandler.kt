package io.github.pavlentygood.cellcapture.lobby.app.input.rest

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
}
