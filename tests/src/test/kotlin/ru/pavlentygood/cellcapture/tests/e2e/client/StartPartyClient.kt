package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.lobby.rest.api.StartPartyApi

@FeignClient(
    name = "start-party-client",
    url = "\${lobby.url}"
)
interface StartPartyClient : StartPartyApi
