plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(":party:domain"))
    implementation(project(":party:usecase"))
    implementation(project(":party:rest"))
    implementation(project(":party:persistence"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":party:domain")))
    testImplementation(testFixtures(project(":party:rest")))
}
