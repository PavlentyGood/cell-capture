package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import io.github.pavlentygood.cellcapture.game.rest.api.GetPartyApi
import io.github.pavlentygood.cellcapture.game.rest.api.RollDicesApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "game-rest-client",
    url = "\${game.url}"
)
interface GameRestClient : GameApiAggregator

interface GameApiAggregator : RollDicesApi, CaptureCellsApi, GetPartyApi
