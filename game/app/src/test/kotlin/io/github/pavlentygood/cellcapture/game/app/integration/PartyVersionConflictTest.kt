package io.github.pavlentygood.cellcapture.game.app.integration

import io.github.pavlentygood.cellcapture.game.app.integration.config.BasePostgresTest
import io.github.pavlentygood.cellcapture.game.app.integration.config.IntegrationConfig
import io.github.pavlentygood.cellcapture.game.domain.party
import io.github.pavlentygood.cellcapture.game.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.kernel.common.VersionConflictException
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [IntegrationConfig::class])
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
