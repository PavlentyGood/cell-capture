package io.github.pavlentygood.cellcapture.ai.app.output.db.dto

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "forests")
class ForestDto(
    @Id
    @Column("id")
    val forestId: UUID,

    val version: Long,

    val name: String
) : Persistable<UUID> {

    override fun getId() = forestId

    override fun isNew() = version == 1L
}
