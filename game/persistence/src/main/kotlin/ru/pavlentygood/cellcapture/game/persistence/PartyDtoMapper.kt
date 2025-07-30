package ru.pavlentygood.cellcapture.game.persistence

import java.sql.ResultSet
import java.util.*

val partyDtoMapper = { rs: ResultSet, _: Int ->
    PartyDto(
        id = rs.getObject("id", UUID::class.java),
        completed = rs.getBoolean("completed"),
        firstDice = rs.getIntOrNull("first_dice"),
        secondDice = rs.getIntOrNull("second_dice"),
        ownerId = rs.getInt("owner_id"),
        currentPlayerId = rs.getInt("current_player_id"),
    )
}

fun ResultSet.getIntOrNull(columnName: String) =
    getObject(columnName, Integer::class.java)?.toInt()
