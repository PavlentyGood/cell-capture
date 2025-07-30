plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
}
