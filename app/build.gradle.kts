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

    testRuntimeOnly(Lib.junitEngine)
}
