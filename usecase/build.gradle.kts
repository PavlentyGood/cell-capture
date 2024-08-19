dependencies {
    implementation(project(":domain"))

    implementation(Lib.kotlinReflect)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)
}
