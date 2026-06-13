package io.github.pavlentygood.cellcapture.game.app.usecase.port

import io.github.pavlentygood.cellcapture.game.domain.Party

fun interface SaveParty : (Party) -> Unit
