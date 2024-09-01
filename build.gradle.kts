plugins {
    id(Plugin.jvm) version Version.kotlin apply false
    id(Plugin.detekt) version Version.detekt
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
        plugin(Plugin.detekt)
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
                        minimum = BigDecimal("0.9")
                    }
                }
            }
            classDirectories.setFrom(
                files(classDirectories.files.map {
                    fileTree(it) {
                        exclude("**/CellCaptureApplication**")
                    }
                })
            )
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
