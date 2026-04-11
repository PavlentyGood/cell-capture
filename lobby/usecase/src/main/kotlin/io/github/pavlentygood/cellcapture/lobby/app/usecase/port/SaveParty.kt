package io.github.pavlentygood.cellcapture.lobby.app.usecase.port

import io.github.pavlentygood.cellcapture.lobby.domain.Party

fun interface SaveParty : (Party) -> Unit
