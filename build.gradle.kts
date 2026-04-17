import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugin.jvm) version Version.kotlin apply false
    id(Plugin.detekt) version Version.detekt
}

subprojects {
    group = "io.github.pavlentygood.cellcapture"

    repositories {
        mavenCentral()
    }

    apply {
        plugin("java")
        plugin("jacoco")
        plugin("java-test-fixtures")
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
        }

        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget = JvmTarget.valueOf("JVM_${Version.java}")
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }
    }
}
