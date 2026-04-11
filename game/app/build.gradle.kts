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
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))
    implementation(project(Module.gameRestApi))
    implementation(project(Module.gameRestEndpoint))
    implementation(project(Module.gamePersistence))

    implementation(platform(Lib.springBootDependencies))
    implementation(platform(Lib.springCloudDependencies))
    implementation(platform(Lib.junitBom))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springBootStarterJdbc)
    implementation(Lib.springCloudStarterStreamKafka)

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
    testImplementation(testFixtures(project(Module.gameDomain)))
    testImplementation(testFixtures(project(Module.gameRestApi)))
    testImplementation(testFixtures(project(Module.gameRestEndpoint)))
    testImplementation(testFixtures(project(Module.gamePersistence)))

    testFixturesImplementation(testFixtures(project(Module.kernelDomain)))
    testFixturesImplementation(testFixtures(project(Module.gameDomain)))
}
