package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "create-party-client",
    url = "\${lobby.url}"
)
interface CreatePartyClient : CreatePartyApi
