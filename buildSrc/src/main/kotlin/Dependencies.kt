object Version {
    const val kotlin = "1.9.23"
    const val detekt = "1.23.6"
    const val springBoot = "3.3.2"
    const val springDependencyManagement = "1.1.6"
    const val jackson = "2.17.2"
    const val junit = "5.11.0"
    const val kotest = "5.9.0"
    const val mockk = "1.13.12"
    const val h2 = "2.3.232"
    const val arrow = "1.2.4"
    const val kotestArrow = "1.4.0"
}

object Plugin {
    const val jvm = "org.jetbrains.kotlin.jvm"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val springBoot = "org.springframework.boot"
    const val springDependencyManagement = "io.spring.dependency-management"
    const val kotlinSpring = "org.jetbrains.kotlin.plugin.spring"
    const val kotlinJpa = "org.jetbrains.kotlin.plugin.jpa"
}

object Lib {
    const val h2 = "com.h2database:h2:${Version.h2}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
    const val jacksonKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jackson}"
    const val springBootStarterWeb = "org.springframework.boot:spring-boot-starter-web:${Version.springBoot}"
    const val springBootStarterJpa = "org.springframework.boot:spring-boot-starter-data-jpa:${Version.springBoot}"
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Version.springBoot}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Version.junit}"
    const val kotestJUnit = "io.kotest:kotest-runner-junit5:${Version.kotest}"
    const val kotestArrow = "io.kotest.extensions:kotest-assertions-arrow-jvm:${Version.kotestArrow}"
    const val arrow = "io.arrow-kt:arrow-core:${Version.arrow}"
    const val mockk = "io.mockk:mockk:${Version.mockk}"
}
