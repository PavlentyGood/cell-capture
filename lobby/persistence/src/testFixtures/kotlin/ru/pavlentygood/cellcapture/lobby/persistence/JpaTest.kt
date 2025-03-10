package ru.pavlentygood.cellcapture.lobby.persistence

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@DataJpaTest(excludeAutoConfiguration = [TestDatabaseAutoConfiguration::class])
@Transactional(propagation = Propagation.NEVER)
@ContextConfiguration(classes = [JpaTest::class])
@EnableAutoConfiguration
annotation class JpaTest
