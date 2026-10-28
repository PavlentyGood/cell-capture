@file:Suppress("HasPlatformType", "unused")

package io.github.pavlentygood.cellcapture.ai.app.fitness

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.onionArchitecture
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.springframework.web.bind.annotation.RestController

@AnalyzeClasses(
    packages = [Fitness.PROJECT],
    importOptions = [
        Fitness.DoNotIncludeSomePackages::class,
        ImportOption.DoNotIncludeTests::class
    ]
)
class Fitness {

    companion object {
        const val PROJECT = "io.github.pavlentygood.cellcapture.ai"
        private const val KERNEL = "io.github.pavlentygood.cellcapture.kernel"

        const val DOMAIN = "$PROJECT.domain.."
        const val USECASE = "$PROJECT.app.usecase.."
        const val USECASE_PORT = "$PROJECT.app.usecase.port.."
        const val REST = "$PROJECT.app.input.rest.."
        const val DB = "$PROJECT.app.output.db.."

        const val KERNEL_DOMAIN = "$KERNEL.domain.."

        data object Postfix {
            const val ENDPOINT = "Endpoint"
            const val USECASE = "UseCase"
        }
    }

    class DoNotIncludeSomePackages : ImportOption {
        override fun includes(location: Location) =
            PROJECT.replace(".", "/")
                .let {
                    !location.contains("$it/app/config") &&
                            !location.contains("$it/app/outbox")
                }
    }

    @ArchTest
    val noCycleDependencies =
        slices()
            .matching("$PROJECT.(**)")
            .should().beFreeOfCycles()

    @ArchTest
    val onionArchitecture =
        onionArchitecture()
            .domainModels(DOMAIN)
            .domainServices(DOMAIN)
            .applicationServices(USECASE)
            .adapter("rest", REST)
            .adapter("db", DB)

    @ArchTest
    val domainDependencies =
        classes()
            .that().resideInAnyPackage(DOMAIN)
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                KERNEL_DOMAIN,
                DOMAIN,
                "java..",
                "kotlin..",
                "arrow.core..",
                "org.jetbrains.annotations.."
            )

    @ArchTest
    val useCaseDependencies =
        classes()
            .that().resideInAnyPackage(USECASE)
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                KERNEL_DOMAIN,
                DOMAIN,
                USECASE,
                "java..",
                "kotlin..",
                "arrow.core..",
                "org.jetbrains.annotations.."
            )

    @ArchTest
    val endpointNaming =
        classes()
            .that().resideInAPackage(REST)
            .and().areAnnotatedWith(RestController::class.java)
            .should().haveSimpleNameEndingWith(Postfix.ENDPOINT)

    @ArchTest
    val useCasesShouldNotBeAccessedFromUseCases =
        noClasses()
            .that().haveSimpleNameEndingWith(Postfix.USECASE)
            .should().dependOnClassesThat().haveSimpleNameEndingWith(Postfix.USECASE)

    @ArchTest
    val singlePublicMethodInEndpoint =
        classes()
            .that().haveSimpleNameEndingWith(Postfix.ENDPOINT)
            .should(haveSinglePublicMethod())

    @ArchTest
    val singlePublicMethodInUseCase =
        classes()
            .that().haveSimpleNameEndingWith(Postfix.USECASE)
            .should(haveSinglePublicMethod())

    @ArchTest
    val singleMethodInPort =
        classes()
            .that().resideInAPackage(USECASE_PORT)
            .should(haveSinglePublicMethod())

    @ArchTest
    val noExceptionsInDomainAndUseCase =
        classes()
            .that().resideInAnyPackage(DOMAIN, USECASE)
            .should(notThrowAnyException())
}
