package io.github.pavlentygood.cellcapture.lobby.usecase.port

import io.github.pavlentygood.cellcapture.lobby.domain.Party

fun interface SaveParty : (Party) -> Unit
