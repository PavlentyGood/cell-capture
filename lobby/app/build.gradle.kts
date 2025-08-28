plugins {
    id(Plugin.springBoot) version Version.springBoot
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
}

docker {
    name = project.parent!!.name
    copySpec.from("build/libs").into("build/libs")
}

tasks {
    dockerPrepare {
        dependsOn(assemble)
    }
}

dependencyManagement {
    imports {
        mavenBom(Lib.springBootDependencies)
        mavenBom(Lib.springCloudDependencies)
        mavenBom(Lib.junitBom)
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.kernelCommon))
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))
    implementation(project(Module.lobbyRestApi))
    implementation(project(Module.lobbyRestEndpoint))
    implementation(project(Module.lobbyPersistence))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWebByBom)
    implementation(Lib.springBootStarterDataJpaByBom)
    implementation(Lib.springCloudStarterStreamKafka)

    testImplementation(Lib.springBootStarterTestByBom)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)
    testImplementation(Lib.testcontainersJUnit)
    testImplementation(Lib.testcontainersPostgresql)
    testImplementation(Lib.testcontainersKafka)

    testRuntimeOnly(Lib.junitEngineByBom)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.lobbyDomain)))
    testImplementation(testFixtures(project(Module.lobbyRestEndpoint)))
    testImplementation(testFixtures(project(Module.lobbyPersistence)))
}
