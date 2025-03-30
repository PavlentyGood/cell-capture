tasks.jar {
    archiveFileName = "${parent!!.name}-${project.name}.jar"
}

dependencies {
    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)

    testImplementation(Lib.springBootStarterTest)
    testImplementation(Lib.kotestJUnit)
    testImplementation(Lib.kotestArrow)
    testImplementation(Lib.mockk)

    testRuntimeOnly(Lib.junitEngine)
}
