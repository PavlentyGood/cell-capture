---
name: domain-object
description: Применять, когда надо работать c объектами доменной модели.
---

# правила

- агрегат наследует `AggregateRoot<ID, EVENT>` из kernel (пакет base); команда — гвард-клаузы when, мутация состояния, событие через addEvent, результат `Either<OpError, Value>`.
- состояние-машина агрегата — sealed-иерархия состояний (Party → ActiveParty/CompletedParty); терминальное состояние возвращает ошибку.
- value object — data class с `internal constructor` и `companion object { fun from(...) }`, возвращающим Either; ввод нормализуй до проверки (trim); границы — private const в companion; unsafe-фабрика (new) только для истинных инвариантов.
- идентификаторы бери из kernel (PartyId, PlayerId); новый — public constructor, `private val`, примитив через toUUID()/toInt(); не переопределяй toString.
- ошибки: на каждую операцию `sealed interface OpError : DomainError`; реализации — data object внизу файла владельца; общая ошибка реализует несколько интерфейсов.
- события: `sealed interface XEvent : DomainEvent`; data class-события в Events.kt; событие добавляй после успешной мутации.
- фабрика агрегата — класс с `operator fun invoke`; недетерминизм (генерация id, время) выноси в port — fun interface.
- восстановление из бд — топ-функция `restoreX(...): Either<DomainError, Aggregate>` с проверкой инвариантов и цепочками flatMap/map.
- Either из arrow.core: создавай через left()/right(); комбинируй flatMap/map; ошибку преобразуй mapLeft; сайд-эффект onRight; raise/bind/either{} не используй.
- базовые типы, vo и константы бери из kernel:domain, не дублируй.
- без nullable в публичном api; отказ моделируй Either, не null; коллекции на входе — read-only List, мутации прячь в private.

# примеры доменных объектов

- kernel (base-типы, vo, id): kernel/domain/src/main/kotlin/io/github/pavlentygood/cellcapture/kernel/domain/**/*.kt
- малый контекст (эталон): lobby/domain/src/main/kotlin/io/github/pavlentygood/cellcapture/lobby/domain/**/*.kt
- расширенный (sealed-состояния, restore): game/domain/src/main/kotlin/io/github/pavlentygood/cellcapture/game/domain/**/*.kt
- отдавай эти glob'ы в paths при поиске через search_file / search_symbol
- следуй стилистике примеров
