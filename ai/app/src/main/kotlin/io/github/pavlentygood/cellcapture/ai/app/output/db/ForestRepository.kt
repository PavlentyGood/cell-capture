package io.github.pavlentygood.cellcapture.ai.app.output.db

import io.github.pavlentygood.cellcapture.ai.app.output.db.dto.ForestDto
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ForestRepository : CrudRepository<ForestDto, UUID>
