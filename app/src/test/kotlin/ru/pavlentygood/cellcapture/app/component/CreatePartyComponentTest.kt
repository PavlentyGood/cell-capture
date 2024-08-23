package ru.pavlentygood.cellcapture.app.component

import io.kotest.matchers.maps.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.pavlentygood.cellcapture.persistence.SavePartyToDatabase
import ru.pavlentygood.cellcapture.rest.API_V1_PARTIES

@SpringBootTest
@AutoConfigureMockMvc
class CreatePartyComponentTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var partyStore: SavePartyToDatabase

    @Test
    fun `create party`() {
        mockMvc.post(API_V1_PARTIES)
            .andExpect { status { isCreated() } }

        partyStore.store shouldHaveSize 1
    }
}
