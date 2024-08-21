plugins {
    id(Plugin.springKotlin) version Version.kotlin
}

dependencies {
    implementation(project(":domain"))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterJpa)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)
    testRuntimeOnly(Lib.h2)
}
