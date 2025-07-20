plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
}

dependencyManagement {
    imports {
        mavenBom(Lib.springCloudDependencies)
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))
    implementation(project(Module.lobbyRestApi))
    implementation(project(Module.lobbyRestEndpoint))
    implementation(project(Module.lobbyPersistence))
    implementation(project(Module.lobbyPublishing))
    implementation(project(Module.gameRestApi))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springBootStarterDataJpa)
    implementation(Lib.springCloudStarterFeign)
    implementation(Lib.springKafka)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)
    testImplementation(Lib.testcontainersJUnit)
    testImplementation(Lib.testcontainersPostgresql)
    testImplementation(Lib.testcontainersKafka)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.lobbyDomain)))
    testImplementation(testFixtures(project(Module.lobbyRestEndpoint)))
    testImplementation(testFixtures(project(Module.lobbyPersistence)))
    testImplementation(testFixtures(project(Module.lobbyPublishing)))
}
