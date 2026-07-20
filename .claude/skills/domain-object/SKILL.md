---
name: domain-object
description: Применять, когда надо работать c объектами доменной модели.
---

# правила

- работай по DDD (Value Objects, Aggregates, Events).
- используй Either.
- у всех иммутабельных объектов ставь data class.

# примеры доменных объектов

- расположение (main): ai/domain/src/main/kotlin/io/github/pavlentygood/cellcapture/ai/domain/**/*.kt
- отдавай этот glob в paths при поиске через search_file / search_symbol
- следуй стилистике примеров