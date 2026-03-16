plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))

    implementation(platform(Lib.springBootDependencies))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)

    implementation(Lib.springBootStarterJdbc)
    implementation(Lib.postgresql)
    implementation(Lib.flywayPostgresql)
}
