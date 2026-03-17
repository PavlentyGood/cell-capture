package io.github.pavlentygood.cellcapture.tests.e2e.client

import io.github.pavlentygood.cellcapture.game.rest.api.CaptureCellsApi
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    name = "capture-cells-client",
    url = "\${game.url}"
)
interface CaptureCellsClient : CaptureCellsApi
