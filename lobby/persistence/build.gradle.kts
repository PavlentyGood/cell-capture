plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))

    implementation(platform(Lib.springBootDependencies))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)

    implementation(Lib.springBootStarterDataJdbc)
    implementation(Lib.postgresql)
    implementation(Lib.flywayPostgresql)
}
