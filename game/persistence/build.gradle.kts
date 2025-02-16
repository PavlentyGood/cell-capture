plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.kotlinJpa) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)

    implementation(Lib.springBootStarterJdbc)
    implementation(Lib.postgresql)
    implementation(Lib.flywayPostgresql)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.mockk)
    testImplementation(Lib.testcontainersPostgresql)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))

    testFixturesImplementation(Lib.springBootStarterTest)
    testFixturesImplementation(Lib.testcontainersPostgresql)
}
