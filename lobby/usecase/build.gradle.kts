dependencies {
    implementation(project(Module.lobbyDomain))

    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)
    testImplementation(Lib.kotestArrow)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.lobbyDomain)))
}
