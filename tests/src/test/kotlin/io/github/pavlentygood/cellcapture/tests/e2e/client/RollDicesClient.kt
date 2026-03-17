package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "roll-dices-client",
    url = "\${game.url}"
)
interface RollDicesClient : RollDicesApi
