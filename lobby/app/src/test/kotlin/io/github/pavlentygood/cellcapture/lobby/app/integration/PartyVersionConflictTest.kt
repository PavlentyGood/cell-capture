package io.github.pavlentygood.cellcapture.lobby.app.integration

import io.github.pavlentygood.cellcapture.kernel.common.VersionConflictException
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.lobby.domain.party
import io.github.pavlentygood.cellcapture.lobby.usecase.port.SaveParty
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest(classes = [PartyVersionConflictTest::class])
@Import(IntegrationConfig::class)
class PartyVersionConflictTest : BasePostgresTest {

    @Autowired
    lateinit var saveParty: SaveParty

    @Test
    fun `version conflict when saving party`() {
        val party = party()
        saveParty(party)

        shouldThrow<VersionConflictException> {
            saveParty(party)
        }
    }
}
