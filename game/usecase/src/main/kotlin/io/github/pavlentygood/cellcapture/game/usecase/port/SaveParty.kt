package io.github.pavlentygood.cellcapture.game.usecase.port

import io.github.pavlentygood.cellcapture.game.domain.Party

fun interface SaveParty : (Party) -> Unit
