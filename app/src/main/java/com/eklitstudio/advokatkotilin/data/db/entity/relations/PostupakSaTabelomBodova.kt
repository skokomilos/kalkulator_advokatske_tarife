package com.eklitstudio.advokatkotilin.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak
import com.eklitstudio.advokatkotilin.data.db.entity.TabelaBodova

data class PostupakSaTabelomBodova(
    @Embedded val postupak: Postupak,
    @Relation(
        parentColumn = "id",
        entity = TabelaBodova::class,
        entityColumn = "postupakId"
    )
    val tabelaBodova: List<TabelaBodova>
)