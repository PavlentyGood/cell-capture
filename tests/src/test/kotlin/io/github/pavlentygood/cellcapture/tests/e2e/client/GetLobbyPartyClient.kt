package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.rest.api.GetPartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "get-lobby-party-client",
    url = "\${lobby.url}"
)
interface GetLobbyPartyClient : GetPartyApi
