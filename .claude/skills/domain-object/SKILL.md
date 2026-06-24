---
name: domain-object
description: Применять, когда надо работать c объектами доменной модели.
---

# правила

- работаем по DDD (Value Objects, Aggregates).
- используем Either.
- у всех иммутабельных объектов проставлять data class.

# примеры доменных объектов. следуй их стилистике

- агрегат: io.github.pavlentygood.cellcapture.lobby.domain.Party
- VO: io.github.pavlentygood.cellcapture.lobby.domain.PlayerLimit
- VO: io.github.pavlentygood.cellcapture.kernel.domain.PartyId