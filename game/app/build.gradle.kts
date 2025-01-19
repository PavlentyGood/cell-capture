plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(":game:domain"))
    implementation(project(":game:usecase"))
    implementation(project(":game:rest"))
    implementation(project(":game:persistence"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(":game:domain")))
    testImplementation(testFixtures(project(":game:rest")))
}
