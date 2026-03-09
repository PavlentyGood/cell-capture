plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.gameDomain))

    implementation(platform(Lib.springBootDependencies))

    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)
    testImplementation(Lib.kotestArrow)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))
}
