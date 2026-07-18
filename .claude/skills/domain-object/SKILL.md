---
name: domain-object
description: Применять, когда надо работать c объектами доменной модели.
---

# правила

- работай по DDD (Value Objects, Aggregates, Events).
- используй Either.
- у всех иммутабельных объектов ставь data class.

# примеры доменных объектов

- находятся в модуле ai в пакете io.github.pavlentygood.cellcapture.ai.domain
- следуй их стилистике