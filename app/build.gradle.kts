plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(":rest"))
    implementation(project(":usecase"))
    implementation(project(":domain"))
    implementation(project(":persistence"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":rest")))
    testImplementation(testFixtures(project(":domain")))
}
