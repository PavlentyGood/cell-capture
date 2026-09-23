---
name: module-structure
description: Применять, когда надо создать новый модуль (bounded context) или менять структуру и сборку модулей.
---

# правила

- новый контекст — каталог `<name>` с подмодулями `<name>:domain`, `<name>:restapi`, `<name>:app`; регистрируй их в settings.gradle.kts.
- в `<name>/build.gradle.kts` только group для subprojects: `io.github.pavlentygood.cellcapture.<name>`.
- domain — чистый kotlin без spring; зависит от `kernel:domain` и его testFixtures; arrow, kotlin-reflect, spring boot bom.
- restapi — только http-контракты: fun interface эндпоинтов, data class запросов/ответов, константы путей в URL.kt; от domain не зависит.
- app — spring boot приложение; зависит от kernel:domain, kernel:common, `<name>:domain`, `<name>:restapi`; web, cloud-stream-kafka, data-jdbc, postgresql, flyway.
- пакеты app: config, input/rest, input/listening, output/db (+dto), outbox, usecase, usecase/port.
- корневой пакет `io.github.pavlentygood.cellcapture.<name>.<submodule>`; домен без подпакет; один главный тип на файл.
- зависимости и плагины только через константы Lib/Module/Plugin из buildSrc (Dependencies.kt), без литеральных версий.
- ресурсы app: application.yml и flyway-миграции; образ собирай как `bootBuildImage { imageName = project.parent!!.name }`.

# примеры структуры и сборки

- эталонный контекст (main): lobby/{domain,restapi,app}/src/main/kotlin/** и lobby/**/build.gradle.kts
- расширенный контекст (kafka-слушатель, app testFixtures): game/{domain,restapi,app}/src/main/kotlin/** и game/**/build.gradle.kts
- регистрация модулей: settings.gradle.kts; общая сборка: build.gradle.kts, buildSrc/src/main/kotlin/Dependencies.kt
- отдавай эти пути в paths при поиске через search_file / search_symbol
- следуй стилистике примеров
