package ru.pavlentygood.cellcapture.tests.e2e

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.lobby.rest.api.CreatePartyApi

@FeignClient(
    name = "create-party-client",
    url = "http://localhost:8080"
)
interface CreatePartyClient : CreatePartyApi
