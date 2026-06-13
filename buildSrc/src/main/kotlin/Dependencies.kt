@file:Suppress("ConstPropertyName")

object Module {
    private const val kernel = ":kernel"
    private const val lobby = ":lobby"
    private const val game = ":game"

    const val kernelDomain = "$kernel:domain"
    const val kernelCommon = "$kernel:common"

    const val lobbyDomain = "$lobby:domain"
    const val lobbyRestApi = "$lobby:restapi"
    const val lobbyApp = "$lobby:app"

    const val gameDomain = "$game:domain"
    const val gameRestApi = "$game:restapi"
    const val gameApp = "$game:app"
}

object Version {
    const val java = "17"
    const val kotlin = "2.1.21"
    const val detekt = "1.23.6"
    const val springBoot = "3.5.13"
    const val springCloudDependencies = "2025.0.2"
    const val jackson = "2.17.2"
    const val junitBom = "5.13.4"
    const val kotest = "5.9.0"
    const val mockk = "1.13.12"
    const val arrow = "1.2.4"
    const val kotestArrow = "1.4.0"
    const val archUnit = "1.3.0"
    const val postgresql = "42.7.5"
    const val flyway = "11.3.1"
    const val testcontainers = "1.21.4"
    const val cucumberBom = "7.27.0"
    const val slf4jApi = "2.0.17"
}

object Plugin {
    const val jvm = "org.jetbrains.kotlin.jvm"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val springBoot = "org.springframework.boot"
    const val kotlinSpring = "org.jetbrains.kotlin.plugin.spring"
}

object Lib {
    const val springBootDependencies = "org.springframework.boot:spring-boot-dependencies:${Version.springBoot}"
    const val springCloudDependencies = "org.springframework.cloud:spring-cloud-dependencies:${Version.springCloudDependencies}"
    const val junitBom = "org.junit:junit-bom:${Version.junitBom}"
    const val cucumberBom = "io.cucumber:cucumber-bom:${Version.cucumberBom}"

    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jackson}"
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web"
    const val springBootStarterJdbc = "org.springframework.boot:spring-boot-starter-jdbc"
    const val springBootStarterDataJdbc = "org.springframework.boot:spring-boot-starter-data-jdbc"
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test"
    const val springCloudStarterFeign = "org.springframework.cloud:spring-cloud-starter-openfeign"
    const val springCloudStarterStreamKafka = "org.springframework.cloud:spring-cloud-starter-stream-kafka"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine"
    const val junitPlatformSuite = "org.junit.platform:junit-platform-suite"
    const val kotestJUnit = "io.kotest:kotest-runner-junit5:${Version.kotest}"
    const val kotestArrow = "io.kotest.extensions:kotest-assertions-arrow-jvm:${Version.kotestArrow}"
    const val arrow = "io.arrow-kt:arrow-core:${Version.arrow}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
    const val archUnit = "com.tngtech.archunit:archunit-junit5:${Version.archUnit}"
    const val postgresql = "org.postgresql:postgresql:${Version.postgresql}"
    const val flywayPostgresql = "org.flywaydb:flyway-database-postgresql:${Version.flyway}"
    const val testcontainers = "org.testcontainers:testcontainers:${Version.testcontainers}"
    const val testcontainersPostgresql = "org.testcontainers:postgresql:${Version.testcontainers}"
    const val testcontainersKafka = "org.testcontainers:kafka:${Version.testcontainers}"
    const val testcontainersJUnit = "org.testcontainers:junit-jupiter:${Version.testcontainers}"
    const val cucumberJava = "io.cucumber:cucumber-java8"
    const val cucumberSpring = "io.cucumber:cucumber-spring"
    const val cucumberJunitPlatformEngine = "io.cucumber:cucumber-junit-platform-engine"
    const val slf4jApi = "org.slf4j:slf4j-api:${Version.slf4jApi}"
}
