plugins {
    id(Plugin.springBoot) version Version.springBoot
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
    id(Plugin.springKotlin) version Version.kotlin
}

dependencies {
    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testRuntimeOnly(Lib.junitEngine)
}
