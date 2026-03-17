package io.github.pavlentygood.cellcapture.lobby.persistence

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId
import org.springframework.jdbc.core.JdbcTemplate

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
