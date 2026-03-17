package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "join-player-client",
    url = "\${lobby.url}"
)
interface JoinPlayerClient : JoinPlayerApi
