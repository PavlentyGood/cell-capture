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
    implementation(project(Module.lobbyDomain))
    implementation(project(Module.lobbyUseCase))
    implementation(project(Module.lobbyRestApi))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.arrow)
}
