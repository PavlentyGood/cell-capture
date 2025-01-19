dependencies {
    implementation(project(":game:domain"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)
    testImplementation(Lib.kotestArrow)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":game:domain")))
}
