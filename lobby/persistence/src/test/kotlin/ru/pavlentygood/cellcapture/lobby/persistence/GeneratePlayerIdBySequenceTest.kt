package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.playerId
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId

@JpaTest
@Import(value = [GeneratePlayerIdBySequence::class])
class GeneratePlayerIdBySequenceTest : PostgresTestContainer() {

    @Autowired
    private lateinit var generatePlayerId: GeneratePlayerId
    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @Test
    fun `generate player id`() {
        val id = playerId()
        setSequenceValue(id)

        generatePlayerId() shouldBe PlayerId(id.toInt() + 1)
    }

    private fun setSequenceValue(id: PlayerId) {
        jdbcTemplate.queryForObject(
            "select setval('player_id_seq', :value)",
            mapOf("value" to id.toInt()),
            Long::class.java
        )
    }
}
