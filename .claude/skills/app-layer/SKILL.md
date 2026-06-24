---
name: app-layer
description: Применять, когда надо реализовывать app-слой модуля (use case, порты, эндпоинты, адаптеры).
---

# правила

- use case — класс с одним `operator fun invoke`, возвращает `Either<OpUseCaseError, Value>`; зависимости через constructor.
- ошибки use case — своя sealed-иерархия OpUseCaseError; доменные ошибки переводить mapLeft { it.toUseCaseError() }; агрегат сохраняй в onRight.
- порты — одиночные fun interface в usecase/port (GetParty, SaveParty, GetPartyByPlayer).
- endpoint в input/rest реализует контракт из restapi-модуля; Either терминируй fold; ошибка — throw RestException (422 + ErrorResponse(type)); общий обработчик ErrorHandler.
- db-адаптер в output/db: Dto и Mapper отдельными файлами; репозитории spring data jdbc; конфликт версий — VersionConflictException из kernel:common.
- публикация в kafka — transactional outbox: событие пиши в outbox-таблицу в той же транзакции агрегата, отправляет OutboxHandler в пакете outbox (OutboxDto, EventTypeDto).
- потребление из kafka — слушатель в input/listening + data class сообщения; слушатель вызывает use case.
- конфигурация — application-класс + config/Config.kt (бины адаптеров).

# примеры app-слоя

- эталон (outbox, data-jdbc): lobby/app/src/main/kotlin/io/github/pavlentygood/cellcapture/lobby/app/**/*.kt
- расширенный (kafka-слушатель, jdbc): game/app/src/main/kotlin/io/github/pavlentygood/cellcapture/game/app/**/*.kt
- контракты + RestException/ErrorResponse: game/restapi/src/main/kotlin/io/github/pavlentygood/cellcapture/game/restapi/**/*.kt
- отдавай эти glob'ы в paths при поиске через search_file / search_symbol
- следуй стилистике примеров
