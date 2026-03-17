package io.github.pavlentygood.cellcapture.game.persistence

import io.github.pavlentygood.cellcapture.game.domain.Cell
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import java.sql.ResultSet

val cellMapper = { rs: ResultSet, _: Int ->
    Cell(
        playerId = PlayerId(rs.getInt("player_id")),
        x = rs.getInt("x"),
        y = rs.getInt("y")
    )
}
