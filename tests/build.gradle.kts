import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id(Plugin.springBoot) version Version.springBoot apply false
    id(Plugin.kotlinSpring) version Version.kotlin
}

tasks {
    test {
        dependsOn(project(Module.lobbyApp).tasks.withType<BootBuildImage>())
        dependsOn(project(Module.gameApp).tasks.withType<BootBuildImage>())
    }
}

dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.lobbyRestApi))
    implementation(project(Module.gameRestApi))

    implementation(platform(Lib.springBootDependencies))
    implementation(platform(Lib.springCloudDependencies))
    implementation(platform(Lib.junitBom))
    implementation(platform(Lib.cucumberBom))

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
