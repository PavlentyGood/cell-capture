package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.game.rest.api.GetPartyApi

@FeignClient(
    name = "get-game-party-client",
    url = "\${game.url}"
)
interface GetGamePartyClient : GetPartyApi
