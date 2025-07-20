package ru.pavlentygood.cellcapture.tests.e2e

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.lobby.rest.api.JoinPlayerApi

@FeignClient(
    name = "join-player-client",
    url = "\${lobby.url}"
)
interface JoinPlayerClient : JoinPlayerApi
