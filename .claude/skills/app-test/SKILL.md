---
name: app-test
description: Применять, когда надо писать интеграционные, компонентные и fitness-тесты app-модуля.
---

# правила

- базовые классы BasePostgresTest/BaseKafkaTest (testcontainers postgres/kafka) + IntegrationConfig в integration/config; тест — @SpringBootTest + @AutoConfigureMockMvc + @Import(IntegrationConfig).
- http-запросы — mockmvc kotlin dsl; ассерты jsonPath; проверяй статус и тело.
- переиспользуй доменные fixtures из testFixtures модулей; app-фикстуры держи в src/testFixtures app-модуля.
- пиши компонентные тесты сквозных сценариев (несколько эндпоинтов, kafka) и тест конфликта версий.
- fitness-тесты — archunit в fitness/Fitness.kt со своими ArchConditions: разделение input/output, отсутствие циклов, суффиксы Endpoint/UseCase, один публичный метод у endpoint/usecase/port, без исключений в domain/usecase.

# примеры тестов app-модуля

- lobby: lobby/app/src/test/kotlin/io/github/pavlentygood/cellcapture/lobby/app/**/*.kt
- game: game/app/src/test/kotlin/io/github/pavlentygood/cellcapture/game/app/**/*.kt
- отдавай эти glob'ы в paths при поиске через search_file / search_symbol
- следуй стилистике примеров
