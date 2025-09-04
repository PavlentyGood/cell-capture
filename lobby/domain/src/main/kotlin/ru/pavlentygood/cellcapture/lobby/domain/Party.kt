package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.*
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

class Party internal constructor(
    id: PartyId,
    started: Boolean,
    players: List<Player>,
    val playerLimit: PlayerLimit,
    val ownerId: PlayerId
) : AggregateRoot<PartyId, PartyEvent>(id) {

    var started = started
        private set

    private val players = players.toMutableList()

    fun getPlayers() = players.toList()

    fun joinPlayer(
        name: PlayerName,
        generatePlayerId: GeneratePlayerId
    ): Either<JoinPlayerError, PlayerId> =
        when {
            started -> AlreadyStarted.left()
            playerLimit.isReached(players.size) -> PlayerCountLimit.left()
            else -> {
                val player = Player(
                    id = generatePlayerId(),
                    name = name
                )
                players.add(player)
                addEvent(PlayerJoinedEvent(id, player))
                player.id.right()
            }
        }

    fun start(playerId: PlayerId): Either<StartPartyError, Unit> =
        when {
            started -> AlreadyStarted.left()
            playerId != ownerId -> PlayerNotOwner.left()
            players.size < MIN_PLAYER_COUNT -> TooFewPlayers.left()
            else -> {
                started = true
                addPartyStartedEvent()
                Unit.right()
            }
        }

    private fun addPartyStartedEvent() {
        addEvent(
            PartyStartedEvent(
                partyId = id,
                ownerId = ownerId,
                players = players
            )
        )
    }

    sealed interface JoinPlayerError : DomainError
    sealed interface StartPartyError : DomainError

    data object PlayerCountLimit : JoinPlayerError
    data object PlayerNotOwner : StartPartyError
    data object TooFewPlayers : StartPartyError
    data object AlreadyStarted : JoinPlayerError, StartPartyError
}
