dependencies {
    implementation(project(Module.kernelDomain))
    implementation(project(Module.lobbyDomain))

    implementation(Lib.kotlinReflect)
    implementation(Lib.arrow)
}
