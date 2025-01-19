plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))
    implementation(project(Module.lobbyRest))
    implementation(project(Module.lobbyPersistence))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.lobbyDomain)))
    testImplementation(testFixtures(project(Module.lobbyRest)))
}
