package ru.pavlentygood.cellcapture.game.persistence

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.game.domain.RestoreParty

@JdbcTest
@Transactional(propagation = Propagation.NEVER)
@ContextConfiguration(classes = [PersistenceTest::class])
@ImportAutoConfiguration(exclude = [TestDatabaseAutoConfiguration::class])
@Import(value = [SavePartyToDatabase::class, GetPartyByPlayerFromDatabase::class, RestoreParty::class])
annotation class PersistenceTest
