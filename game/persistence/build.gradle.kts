plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.kotlinJpa) version Version.kotlin
}

dependencies {
    implementation(project(":game:domain"))
    implementation(project(":game:usecase"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":game:domain")))
}
