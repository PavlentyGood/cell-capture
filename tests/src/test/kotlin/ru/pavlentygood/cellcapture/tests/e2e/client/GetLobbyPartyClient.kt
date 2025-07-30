package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.lobby.rest.api.GetPartyApi

@FeignClient(
    name = "get-lobby-party-client",
    url = "\${lobby.url}"
)
interface GetLobbyPartyClient : GetPartyApi
