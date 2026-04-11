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
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))
    implementation(project(Module.lobbyRestApi))
    implementation(project(Module.lobbyRestEndpoint))
    implementation(project(Module.lobbyPersistence))

    implementation(platform(Lib.springBootDependencies))
    implementation(platform(Lib.springCloudDependencies))
    implementation(platform(Lib.junitBom))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springCloudStarterStreamKafka)

    implementation(Lib.springBootStarterDataJdbc)
    implementation(Lib.postgresql)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)
    testImplementation(Lib.testcontainers)
    testImplementation(Lib.testcontainersJUnit)
    testImplementation(Lib.testcontainersPostgresql)
    testImplementation(Lib.testcontainersKafka)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.kernelCommon)))
    testImplementation(testFixtures(project(Module.lobbyDomain)))
    testImplementation(testFixtures(project(Module.lobbyRestEndpoint)))
    testImplementation(testFixtures(project(Module.lobbyPersistence)))
}
