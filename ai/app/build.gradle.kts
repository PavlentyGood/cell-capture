plugins {
    id(Plugin.springBoot) version Version.springBoot
    id(Plugin.kotlinSpring) version Version.kotlin
}

tasks {
    bootBuildImage {
        imageName = project.parent!!.name
        environment.put("BP_JVM_VERSION", Version.java)
    }
}

dependencies {
    implementation(project(":ai:domain"))
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))

    implementation(platform(Lib.springBootDependencies))
    implementation(platform(Lib.springCloudDependencies))
    implementation(platform(Lib.junitBom))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springCloudStarterStreamKafka)

    implementation(Lib.springBootStarterDataJdbc)
    implementation(Lib.postgresql)
    implementation(Lib.flywayPostgresql)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)
    testImplementation(Lib.testcontainers)
    testImplementation(Lib.testcontainersPostgresql)
    testImplementation(Lib.testcontainersKafka)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.kernelCommon)))

    testFixturesImplementation(testFixtures(project(Module.kernelDomain)))
}
