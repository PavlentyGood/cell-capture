package ru.pavlentygood.cellcapture.game.app.integration

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.pavlentygood.cellcapture.game.app.integration.config.IntegrationConfig
import ru.pavlentygood.cellcapture.game.domain.party
import ru.pavlentygood.cellcapture.game.app.integration.config.BasePostgresTest
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.common.VersionConflictException

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
