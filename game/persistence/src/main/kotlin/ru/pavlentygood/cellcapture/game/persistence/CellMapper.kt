package ru.pavlentygood.cellcapture.game.persistence

import ru.pavlentygood.cellcapture.game.domain.Cell
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import java.sql.ResultSet

val cellMapper = { rs: ResultSet, _: Int ->
    Cell(
        playerId = PlayerId(rs.getInt("player_id")),
        x = rs.getInt("x"),
        y = rs.getInt("y")
    )
}
