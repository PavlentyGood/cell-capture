@file:Suppress("ConstPropertyName")

object Module {
    private const val kernel = ":kernel"
    private const val lobby = ":lobby"
    private const val game = ":game"

    const val kernelDomain = "$kernel:domain"

    const val lobbyDomain = "$lobby:domain"
    const val lobbyUseCase = "$lobby:usecase"
    const val lobbyRestApi = "$lobby:rest:api"
    const val lobbyRestEndpoint = "$lobby:rest:endpoint"
    const val lobbyPersistence = "$lobby:persistence"
    const val lobbyPublishing = "$lobby:publishing"
    const val lobbyApp = "$lobby:app"

    const val gameDomain = "$game:domain"
    const val gameUseCase = "$game:usecase"
    const val gameRestApi = "$game:rest:api"
    const val gameRestEndpoint = "$game:rest:endpoint"
    const val gamePersistence = "$game:persistence"
    const val gameListening = "$game:listening"
    const val gameApp = "$game:app"
}

object Version {
    const val kotlin = "1.9.23"
    const val detekt = "1.23.6"
    const val springBoot = "3.3.2"
    const val springDependencyManagement = "1.1.7"
    const val springCloudDependencies = "2024.0.1"
    const val jackson = "2.17.2"
    const val junit = "5.11.0"
    const val kotest = "5.9.0"
    const val mockk = "1.13.12"
    const val arrow = "1.2.4"
    const val kotestArrow = "1.4.0"
    const val archUnit = "1.3.0"
    const val postgresql = "42.7.5"
    const val flyway = "11.3.1"
    const val testcontainers = "1.20.5"
    const val docker = "0.36.0"
}

object Plugin {
    const val jvm = "org.jetbrains.kotlin.jvm"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val springBoot = "org.springframework.boot"
    const val kotlinSpring = "org.jetbrains.kotlin.plugin.spring"
    const val springDependencyManagement = "io.spring.dependency-management"
    const val kotlinJpa = "org.jetbrains.kotlin.plugin.jpa"
    const val docker = "com.palantir.docker"
}

object Lib {
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jackson}"
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Version.springBoot}"
    const val springBootStarterJdbc = "org.springframework.boot:spring-boot-starter-jdbc:${Version.springBoot}"
    const val springBootStarterDataJpa = "org.springframework.boot:spring-boot-starter-data-jpa:${Version.springBoot}"
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Version.springBoot}"
    const val springCloudStarterFeign = "org.springframework.cloud:spring-cloud-starter-openfeign"
    const val springCloudDependencies = "org.springframework.cloud:spring-cloud-dependencies:${Version.springCloudDependencies}"
    const val springKafka = "org.springframework.kafka:spring-kafka:${Version.springBoot}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Version.junit}"
    const val kotestJUnit = "io.kotest:kotest-runner-junit5:${Version.kotest}"
    const val kotestArrow = "io.kotest.extensions:kotest-assertions-arrow-jvm:${Version.kotestArrow}"
    const val arrow = "io.arrow-kt:arrow-core:${Version.arrow}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
    const val archUnit = "com.tngtech.archunit:archunit-junit5:${Version.archUnit}"
    const val postgresql = "org.postgresql:postgresql:${Version.postgresql}"
    const val flywayPostgresql = "org.flywaydb:flyway-database-postgresql:${Version.flyway}"
    const val testcontainersPostgresql = "org.testcontainers:postgresql:${Version.testcontainers}"
    const val testcontainersKafka = "org.testcontainers:kafka:${Version.testcontainers}"
    const val testcontainersJUnit = "org.testcontainers:junit-jupiter:${Version.testcontainers}"
}
