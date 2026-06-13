package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.restapi.CreatePartyApi
import io.github.pavlentygood.cellcapture.lobby.restapi.GetPartyApi
import io.github.pavlentygood.cellcapture.lobby.restapi.JoinPlayerApi
import io.github.pavlentygood.cellcapture.lobby.restapi.StartPartyApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "lobby-rest-client",
    url = "\${lobby.url}"
)
interface LobbyRestClient : LobbyApiAggregator

interface LobbyApiAggregator : CreatePartyApi, JoinPlayerApi, StartPartyApi, GetPartyApi
