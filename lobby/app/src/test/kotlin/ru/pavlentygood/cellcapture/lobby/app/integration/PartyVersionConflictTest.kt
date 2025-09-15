package ru.pavlentygood.cellcapture.lobby.app.integration

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.pavlentygood.cellcapture.kernel.common.VersionConflictException
import ru.pavlentygood.cellcapture.lobby.app.integration.config.IntegrationConfig
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@SpringBootTest(classes = [IntegrationConfig::class])
class PartyVersionConflictTest {

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
