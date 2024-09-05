plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(":usecase"))
    implementation(project(":domain"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":domain")))
}
