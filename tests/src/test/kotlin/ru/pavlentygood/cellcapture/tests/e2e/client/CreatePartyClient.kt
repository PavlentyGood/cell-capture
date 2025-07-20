package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyApi

@FeignClient(
    name = "create-party-client",
    url = "\${lobby.url}"
)
interface CreatePartyClient : CreatePartyApi
