package io.github.pavlentygood.cellcapture.game.rest.api

import org.springframework.http.ResponseEntity

class RestException(
    val responseEntity: ResponseEntity<*>
) : Exception()
