import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugin.jvm) version Version.kotlin apply false
    id(Plugin.detekt) version Version.detekt
    id(Plugin.docker) version Version.docker
}

subprojects {
    group = "ru.pavlentygood.cellcapture"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
        plugin("jacoco")
        plugin("java-test-fixtures")
        plugin(Plugin.jvm)
        plugin(Plugin.detekt)
        plugin(Plugin.docker)
    }

    detekt {
        buildUponDefaultConfig = true

        dependencies {
            detektPlugins("${Plugin.detektFormatting}:${Version.detekt}")
        }
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
                        minimum = BigDecimal("0.0")
                    }
                }
            }
            classDirectories.setFrom(
                files(classDirectories.files.map {
                    fileTree(it) {
                        exclude(listOf(
                            "**/**Application**",
                            "**/persistence/**",
                            "**/kernel/domain/base/**"
                        ))
                    }
                })
            )
        }

        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "17"
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
