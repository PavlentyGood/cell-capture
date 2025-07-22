package ru.pavlentygood.cellcapture.tests.e2e.client

import org.springframework.cloud.openfeign.FeignClient
import ru.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi

@FeignClient(
    name = "capture-cells-client",
    url = "\${game.url}"
)
interface CaptureCellsClient : CaptureCellsApi
