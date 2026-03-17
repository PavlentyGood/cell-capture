@file:Suppress("HasPlatformType", "unused")

package io.github.pavlentygood.cellcapture.game.app.fitness

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
    packages = ["io.github.pavlentygood.cellcapture.game"],
    importOptions = [Fitness.DoNotIncludeAppPackage::class]
)
class Fitness {

    class DoNotIncludeAppPackage : ImportOption {
        override fun includes(location: Location) =
            !location.contains("io/github/pavlentygood/cellcapture/game/app/")
    }

    /**
     * Остерегаемся структур с циклическими зависимостями
     */
    @ArchTest
    val noCycleDependencies =
        slices()
            .matching("io.github.pavlentygood.cellcapture.game.(**)")
            .should().beFreeOfCycles()

    /**
     * Базовый паттерн для Чистой архитектуры
     */
    @ArchTest
    val onionArchitecture =
        onionArchitecture()
            .domainModels("io.github.pavlentygood.cellcapture.game.domain..")
            .domainServices("io.github.pavlentygood.cellcapture.game.domain..")
            .applicationServices("io.github.pavlentygood.cellcapture.game.usecase..")
            .adapter("rest", "io.github.pavlentygood.cellcapture.game.rest..")
            .adapter("persistence", "io.github.pavlentygood.cellcapture.game.persistence..")

    /**
     * Доменная модель имеет минимальное количество зависимостей
     */
    @ArchTest
    val domainDependencies =
        classes()
            .that().resideInAnyPackage("io.github.pavlentygood.cellcapture.game.domain..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "io.github.pavlentygood.cellcapture.kernel.domain..",
                "io.github.pavlentygood.cellcapture.game.domain..",
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
            .that().resideInAnyPackage("io.github.pavlentygood.cellcapture.game.usecase..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "io.github.pavlentygood.cellcapture.kernel.domain..",
                "io.github.pavlentygood.cellcapture.game.domain..",
                "io.github.pavlentygood.cellcapture.game.usecase..",
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
            .that().resideInAPackage("io.github.pavlentygood.cellcapture.game.rest..")
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

    /**
     * В классах эндпоинтов не более одного публичного метода
     */
    @ArchTest
    val singlePublicMethodInEndpoint =
        classes()
            .that().haveSimpleNameEndingWith("Endpoint")
            .should(haveSinglePublicMethod())

    /**
     * В юскейсах не более одного публичного метода
     */
    @ArchTest
    val singlePublicMethodInUseCase =
        classes()
            .that().haveSimpleNameEndingWith("UseCase")
            .should(haveSinglePublicMethod())

    /**
     * В портах не более одного метода
     */
    @ArchTest
    val singleMethodInPort =
        classes()
            .that().resideInAPackage("io.github.pavlentygood.cellcapture.game.usecase.port..")
            .should(haveSinglePublicMethod())

    /**
     * В доменной модели и в юскейсах отсутствуют исключения
     */
    @ArchTest
    val noExceptionsInDomainAndUseCase =
        classes()
            .that().resideInAnyPackage(
                "io.github.pavlentygood.cellcapture.game.domain..",
                "io.github.pavlentygood.cellcapture.game.usecase.."
            ).should(notThrowAnyException())
}
