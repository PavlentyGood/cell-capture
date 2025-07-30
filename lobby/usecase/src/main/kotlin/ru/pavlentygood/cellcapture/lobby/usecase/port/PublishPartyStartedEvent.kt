package ru.pavlentygood.cellcapture.lobby.usecase.port

import ru.pavlentygood.cellcapture.lobby.domain.Party

fun interface PublishPartyStartedEvent : (Party) -> Unit
