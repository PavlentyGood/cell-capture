package io.github.pavlentygood.cellcapture.ai.app.usecase.port

import io.github.pavlentygood.cellcapture.ai.domain.Forest

fun interface SaveForest : (Forest) -> Unit
