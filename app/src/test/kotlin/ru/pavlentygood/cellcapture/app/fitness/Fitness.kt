package ru.pavlentygood.cellcapture.app.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.springframework.web.bind.annotation.RestController

@Suppress("HasPlatformType")
@AnalyzeClasses(
    packages = ["ru.pavlentygood.cellcapture"],
    importOptions = [Fitness.DoNotIncludeAppPackage::class]
)
class Fitness {

    class DoNotIncludeAppPackage : ImportOption {
        override fun includes(location: Location) =
            !location.contains("ru/pavlentygood/cellcapture/app/")
    }

    @ArchTest
    val noCycleDependencies =
        SlicesRuleDefinition.slices()
            .matching("ru.pavlentygood.cellcapture.(**)")
            .should().beFreeOfCycles()

    @ArchTest
    val onionArchitecture =
        Architectures
            .onionArchitecture()
            .domainModels("..domain..")
            .domainServices("..domain..")
            .applicationServices("..usecase..")
            .adapter("rest", "..rest..")
            .adapter("persistence", "..persistence..")

    @ArchTest
    val domainDependencies =
        ArchRuleDefinition.classes()
            .that().resideInAnyPackage("..domain..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..domain..",
                "java..",
                "kotlin..",
                "arrow.core..",
                "org.jetbrains.annotations.."
            )

    @ArchTest
    val useCaseDependencies =
        ArchRuleDefinition.classes()
            .that().resideInAnyPackage("..usecase..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..domain..",
                "..usecase..",
                "java..",
                "kotlin..",
                "arrow.core..",
                "org.jetbrains.annotations.."
            )

    @ArchTest
    val endpointNaming =
        ArchRuleDefinition.classes()
            .that().resideInAPackage("..rest..")
            .and().areAnnotatedWith(RestController::class.java)
            .should().haveSimpleNameEndingWith("Endpoint")
}
