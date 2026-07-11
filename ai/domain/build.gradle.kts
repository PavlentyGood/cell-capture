dependencies {
    implementation(project(Module.kernelDomain))

    implementation(platform(Lib.springBootDependencies))

    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))

    testFixturesImplementation(testFixtures(project(Module.kernelDomain)))
    testFixturesImplementation(Lib.arrow)
}
