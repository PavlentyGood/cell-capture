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
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))
    implementation(project(Module.gameRestApi))
    implementation(project(Module.gameRestEndpoint))
    implementation(project(Module.gamePersistence))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.arrow)
    implementation(Lib.springBootStarterWebByBom)
    implementation(Lib.springBootStarterJdbcByBom)
    implementation(Lib.springCloudStarterStreamKafka)

    testImplementation(Lib.springBootStarterTestByBom)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.archUnit)
    testImplementation(Lib.testcontainersKafka)

    testRuntimeOnly(Lib.junitEngineByBom)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))
    testImplementation(testFixtures(project(Module.gameRestApi)))
    testImplementation(testFixtures(project(Module.gameRestEndpoint)))
    testImplementation(testFixtures(project(Module.gamePersistence)))
}
