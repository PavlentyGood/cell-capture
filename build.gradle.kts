plugins {
    kotlin("plugin.spring") version Version.kotlin
    kotlin("plugin.jpa") version Version.kotlin
    id(Plugin.jvm) version Version.kotlin apply false
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

subprojects {
    group = "ru.pavlentygood.cellcapture"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
        plugin("jacoco")
        plugin(Plugin.jvm)
    }

    tasks {
        val check = named<DefaultTask>("check")
        val jacocoTestReport = named<JacocoReport>("jacocoTestReport")
        val jacocoTestCoverageVerification = named<JacocoCoverageVerification>("jacocoTestCoverageVerification")

        check {
            finalizedBy(jacocoTestReport)
        }

        jacocoTestReport {
            dependsOn(check)
            finalizedBy(jacocoTestCoverageVerification)
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)
            violationRules {
                rule {
                    limit {
                        minimum = BigDecimal("0.9")
                    }
                }
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
