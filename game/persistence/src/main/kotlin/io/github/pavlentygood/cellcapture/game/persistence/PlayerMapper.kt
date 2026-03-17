package io.github.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import io.github.pavlentygood.cellcapture.game.domain.Player
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
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
