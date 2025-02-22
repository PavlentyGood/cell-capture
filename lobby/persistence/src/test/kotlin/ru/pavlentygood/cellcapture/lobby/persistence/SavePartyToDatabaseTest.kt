package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@DataJpaTest(excludeAutoConfiguration = [TestDatabaseAutoConfiguration::class])
@Transactional(propagation = Propagation.NEVER)
@ContextConfiguration(classes = [TestPersistenceConfig::class])
class SavePartyToDatabaseTest {

    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate
    @Autowired
    private lateinit var saveParty: SaveParty

    @Test
    fun `save party`() {
        val party = party()

        saveParty(party)

        party.checkExistsInDb()
        party.getPlayers().forEach { it.checkExistsInDb() }
    }

    private fun Party.checkExistsInDb() {
        val fields = jdbcTemplate.queryForMap(
            "select * from parties where id = :id",
            mapOf("id" to id.toUUID())
        )
        fields["started"] shouldBe false
        fields["owner_id"] shouldBe ownerId.toInt()
        fields["player_limit"] shouldBe playerLimit.value
    }

    private fun Player.checkExistsInDb() {
        val fields = jdbcTemplate.queryForMap(
            "select * from players where id = :id",
            mapOf("id" to id.toInt())
        )
        fields["name"] shouldBe name.toStringValue()
    }
}
