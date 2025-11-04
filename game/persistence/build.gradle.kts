plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
}

dependencyManagement {
    imports {
        mavenBom(Lib.springBootDependencies)
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)

    implementation(Lib.springBootStarterJdbc)
    implementation(Lib.postgresql)
    implementation(Lib.flywayPostgresql)

    testFixturesImplementation(Lib.springBootStarterTest)
    testFixturesImplementation(Lib.testcontainersPostgresql)
}
