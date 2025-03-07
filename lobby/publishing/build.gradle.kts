plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)
    implementation(Lib.springKafka)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.lobbyDomain)))
    testImplementation(testFixtures(project(Module.lobbyPublishing)))

    testFixturesImplementation(Lib.springBootStarterTest)
    testFixturesImplementation(Lib.testcontainersKafka)
    testFixturesImplementation(Lib.testcontainersJUnit)
}
