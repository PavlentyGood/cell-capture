package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.game.rest.api.RollDicesApi

@FeignClient(
    name = "roll-dices-client",
    url = "\${game.url}"
)
interface RollDicesClient : RollDicesApi
