@file:Suppress("HasPlatformType")

package ru.pavlentygood.cellcapture.lobby.app.fitness

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
    importOptions = [Fitness.DoNotIncludeAppPackage::class]
)
class Fitness {

    companion object {
        const val PROJECT = "ru.pavlentygood.cellcapture.lobby"
        private const val KERNEL = "ru.pavlentygood.cellcapture.kernel"

        const val DOMAIN = "$PROJECT.domain.."
        const val USECASE = "$PROJECT.usecase.."
        const val REST = "$PROJECT.rest.."
        const val PERSISTENCE = "$PROJECT.persistence.."

        const val KERNEL_DOMAIN = "$KERNEL.domain.."
    }

    class DoNotIncludeAppPackage : ImportOption {
        override fun includes(location: Location) =
            PROJECT.replace(".", "/")
                .let { !location.contains("$it/app/") }
    }

    /**
     * Остерегаемся структур с циклическими зависимостями
     */
    @ArchTest
    val noCycleDependencies =
        slices()
            .matching("$PROJECT.(**)")
            .should().beFreeOfCycles()

    /**
     * Базовый паттерн для Чистой архитектуры
     */
    @ArchTest
    val onionArchitecture =
        onionArchitecture()
            .domainModels(DOMAIN)
            .domainServices(DOMAIN)
            .applicationServices(USECASE)
            .adapter("rest", REST)
            .adapter("persistence", PERSISTENCE)

    /**
     * Доменный слой имеет минимальное количество зависимостей
     */
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

    /**
     * Юскейсы имеют минимальное количество зависимостей
     */
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

    /**
     * Классы эндпоинтов имеют определенный постфикс
     */
    @ArchTest
    val endpointNaming =
        classes()
            .that().resideInAPackage(REST)
            .and().areAnnotatedWith(RestController::class.java)
            .should().haveSimpleNameEndingWith("Endpoint")

    /**
     * Юскейсы не вызывают другие юскейсы
     */
    @ArchTest
    val useCasesShouldNotBeAccessedFromUseCases =
        noClasses()
            .that().haveSimpleNameEndingWith("UseCase")
            .should().dependOnClassesThat().haveSimpleNameEndingWith("UseCase")
}
