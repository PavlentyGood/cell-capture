package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import java.sql.ResultSet

val playerMapper = { rs: ResultSet, _: Int ->
    val name = rs.getString("name")
    Player(
        id = PlayerId(rs.getInt("id")),
        name = PlayerName.from(name).getOrElse {
            error("Illegal player name: $name")
        }
    )
}
