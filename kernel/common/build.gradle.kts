tasks.jar {
    archiveFileName = "${parent!!.name}-${project.name}.jar"
}

dependencies {
    implementation(Lib.kotlinReflect)
    implementation(Lib.slf4jApi)
}
