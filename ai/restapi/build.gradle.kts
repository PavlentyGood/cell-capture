plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(platform(Lib.springBootDependencies))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
}
