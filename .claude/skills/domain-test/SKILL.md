---
name: domain-test
description: Применять, когда надо работать с тестами для доменной модели.
---

# правила

- работай по детройтской школе: реальные объекты, без моков; стаб-лямбда только для недетерминизма (генерация id).
- названия методов — факты в настоящем времени без технических деталей; негативный случай через " - " (`join player - party already started`).
- класс `<Subject>Test` в пакете домена; для топ-функций суффикс KtTest (RestorePartyKtTest).
- junit 5 (@Test); границы vo проверяй @ParameterizedTest + @ValueSource.
- ассерты kotest: shouldBe, shouldNotBe, shouldContainExactly, shouldHaveSize; для Either — shouldBeRight/shouldBeLeft из io.kotest.assertions.arrow.core; shouldBeRight() возвращает значение.
- error-кейс: проверяй Left и постусловия — popEvents пуст, состояние не изменилось.
- структура: плоские arrange-act-assert с пустой строкой между фазами; тест-двойники объявляй внизу тестового файла.
- fixtures — src/testFixtures/kotlin, один Fixtures.kt в пакете домена; топ-фабрики с именем типа, все параметры опциональны (named-аргументы задают given); уникальность через randomInt/счётчик; Either анврапивай .get() из kernel fixtures.
- сначала пиши тесты; после кода запускай gradlew test, check (покрытие 0.9), detekt.

# примеры доменных тестов

- kernel (включая fixtures): kernel/domain/src/test*/kotlin/io/github/pavlentygood/cellcapture/kernel/domain/**/*.kt
- lobby: lobby/domain/src/test*/kotlin/io/github/pavlentygood/cellcapture/lobby/domain/**/*.kt
- game: game/domain/src/test*/kotlin/io/github/pavlentygood/cellcapture/game/domain/**/*.kt
- отдавай эти glob'ы в paths при поиске через search_file / search_symbol
- следуй стилистике примеров
