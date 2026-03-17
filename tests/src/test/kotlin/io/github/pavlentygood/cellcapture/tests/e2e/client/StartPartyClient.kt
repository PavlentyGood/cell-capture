package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.rest.api.StartPartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "start-party-client",
    url = "\${lobby.url}"
)
interface StartPartyClient : StartPartyApi
