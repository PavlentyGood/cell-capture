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
    implementation(project(Module.gameDomain))
    implementation(project(Module.gameUseCase))
    implementation(project(Module.gameRestApi))

    implementation(Lib.kotlinReflect)
    implementation(Lib.jacksonKotlin)
    implementation(Lib.springBootStarterWeb)
    implementation(Lib.arrow)
}
