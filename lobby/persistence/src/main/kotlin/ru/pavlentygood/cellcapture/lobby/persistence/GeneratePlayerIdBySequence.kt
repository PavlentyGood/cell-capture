package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.jdbc.core.JdbcTemplate
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId

class GeneratePlayerIdBySequence(
    private val jdbcTemplate: JdbcTemplate
) : GeneratePlayerId {

    override fun invoke(): PlayerId {
        val id = jdbcTemplate.queryForObject(
            "select nextval('player_id_seq')",
            Int::class.java
        )!!
        return PlayerId(id)
    }
}
