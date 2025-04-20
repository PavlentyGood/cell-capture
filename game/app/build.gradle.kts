plugins {
    id(Plugin.springBoot) version Version.springBoot
    id(Plugin.kotlinSpring) version Version.kotlin
}

docker {
    name = project.parent!!.name
    copySpec.from("build/libs").into("build/libs")
}

tasks {
    dockerPrepare {
        dependsOn(build)
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))
    implementation(project(Module.gameRestApi))
    implementation(project(Module.gameRestEndpoint))
    implementation(project(Module.gamePersistence))
    implementation(project(Module.gameListening))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springKafka)

    implementation(Lib.springBootStarterJdbc)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))
    testImplementation(testFixtures(project(Module.gameRestApi)))
    testImplementation(testFixtures(project(Module.gameRestEndpoint)))
    testImplementation(testFixtures(project(Module.gamePersistence)))
    testImplementation(testFixtures(project(Module.gameListening)))
}
