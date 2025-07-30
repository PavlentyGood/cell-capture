@file:Suppress("HasPlatformType")

package ru.pavlentygood.cellcapture.game.app.fitness

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
    packages = ["ru.pavlentygood.cellcapture.game"],
    importOptions = [Fitness.DoNotIncludeAppPackage::class]
)
class Fitness {

    class DoNotIncludeAppPackage : ImportOption {
        override fun includes(location: Location) =
            !location.contains("ru/pavlentygood/cellcapture/game/app/")
    }

    /**
     * Остерегаемся структур с циклическими зависимостями
     */
    @ArchTest
    val noCycleDependencies =
        slices()
            .matching("ru.pavlentygood.cellcapture.game.(**)")
            .should().beFreeOfCycles()

    /**
     * Базовый паттерн для Чистой архитектуры
     */
    @ArchTest
    val onionArchitecture =
        onionArchitecture()
            .domainModels("ru.pavlentygood.cellcapture.game.domain..")
            .domainServices("ru.pavlentygood.cellcapture.game.domain..")
            .applicationServices("ru.pavlentygood.cellcapture.game.usecase..")
            .adapter("rest", "ru.pavlentygood.cellcapture.game.rest..")
            .adapter("persistence", "ru.pavlentygood.cellcapture.game.persistence..")
            .adapter("listening", "ru.pavlentygood.cellcapture.game.listening..")

    /**
     * Доменный слой имеет минимальное количество зависимостей
     */
    @ArchTest
    val domainDependencies =
        classes()
            .that().resideInAnyPackage("ru.pavlentygood.cellcapture.game.domain..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "ru.pavlentygood.cellcapture.kernel.domain..",
                "ru.pavlentygood.cellcapture.game.domain..",
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
            .that().resideInAnyPackage("ru.pavlentygood.cellcapture.game.usecase..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "ru.pavlentygood.cellcapture.kernel.domain..",
                "ru.pavlentygood.cellcapture.game.domain..",
                "ru.pavlentygood.cellcapture.game.usecase..",
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
            .that().resideInAPackage("ru.pavlentygood.cellcapture.game.rest..")
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
