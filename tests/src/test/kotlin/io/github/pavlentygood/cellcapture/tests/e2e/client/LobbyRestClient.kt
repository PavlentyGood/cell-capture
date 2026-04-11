package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.rest.api.CreatePartyApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.GetPartyApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerApi
import io.github.pavlentygood.cellcapture.lobby.rest.api.StartPartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "lobby-rest-client",
    url = "\${lobby.url}"
)
interface LobbyRestClient : LobbyApiAggregator

interface LobbyApiAggregator : CreatePartyApi, JoinPlayerApi, StartPartyApi, GetPartyApi
