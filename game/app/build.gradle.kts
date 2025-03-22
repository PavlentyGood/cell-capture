plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))
    implementation(project(Module.gameRest))
    implementation(project(Module.gamePersistence))
    implementation(project(Module.gameListening))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springKafka)

    implementation(Lib.springBootStarterJdbc)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))
    testImplementation(testFixtures(project(Module.gameRest)))
    testImplementation(testFixtures(project(Module.gamePersistence)))
    testImplementation(testFixtures(project(Module.gameListening)))
}
