plugins {
    id(Plugin.kotlinSpring) version Version.kotlin
    id(Plugin.springDependencyManagement) version Version.springDependencyManagement
}

tasks {
    test {
        dependsOn(project(Module.lobbyApp).tasks.docker)
        dependsOn(project(Module.gameApp).tasks.docker)
    }
}

dependencyManagement {
    imports {
        mavenBom(Lib.springCloudDependencies)
        mavenBom(Lib.junitBom)
        mavenBom(Lib.cucumberBom)
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.lobbyRestApi))
    implementation(project(Module.gameRestApi))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.springCloudStarterFeign)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.arrow)
    testImplementation(Lib.testcontainersJUnit)
    testImplementation(Lib.junitPlatformSuite)
    testImplementation(Lib.cucumberJava)
    testImplementation(Lib.cucumberSpring)
    testImplementation(Lib.cucumberJunitPlatformEngine)

    testRuntimeOnly(Lib.junitEngine)

    testImplementation(testFixtures(project(Module.kernelDomain)))
    testImplementation(testFixtures(project(Module.gameDomain)))
}
