package ru.pavlentygood.cellcapture.game.rest.endpoint

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.pavlentygood.cellcapture.game.rest.api.RestException
import ru.pavlentygood.cellcapture.kernel.common.VersionConflictException
import ru.pavlentygood.cellcapture.kernel.common.log

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
