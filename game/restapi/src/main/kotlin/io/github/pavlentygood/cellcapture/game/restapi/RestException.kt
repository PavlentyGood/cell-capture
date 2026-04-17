package io.github.pavlentygood.cellcapture.game.restapi

import org.springframework.http.ResponseEntity

class RestException(
    val responseEntity: ResponseEntity<*>
) : Exception()
