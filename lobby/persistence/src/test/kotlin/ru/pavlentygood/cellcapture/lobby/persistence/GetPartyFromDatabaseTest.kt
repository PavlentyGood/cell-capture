package ru.pavlentygood.cellcapture.lobby.persistence

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import ru.pavlentygood.cellcapture.kernel.domain.partyId
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.domain.party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

@JpaTest
@Import(value = [
    SavePartyToDatabase::class,
    GetPartyFromDatabase::class,
    MapPartyToDomain::class,
    RestoreParty::class
])
class GetPartyFromDatabaseTest {

    @Autowired
    private lateinit var saveParty: SaveParty
    @Autowired
    private lateinit var getParty: GetParty

    @Test
    fun `get party`() {
        val party = party()
        saveParty(party)

        getParty(party.id)!!.apply {
            id shouldBe party.id
            started shouldBe party.started
            ownerId shouldBe party.ownerId
            playerLimit shouldBe party.playerLimit
            getPlayers() shouldBe party.getPlayers()
        }
    }

    @Test
    fun `get party - not found`() {
        getParty(partyId()) shouldBe null
    }
}
