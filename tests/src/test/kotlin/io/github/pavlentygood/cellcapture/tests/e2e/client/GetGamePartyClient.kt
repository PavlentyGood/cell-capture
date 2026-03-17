package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.game.rest.api.GetPartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "get-game-party-client",
    url = "\${game.url}"
)
interface GetGamePartyClient : GetPartyApi
