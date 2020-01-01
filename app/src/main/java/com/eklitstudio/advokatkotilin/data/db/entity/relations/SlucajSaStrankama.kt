package com.eklitstudio.advokatkotilin.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.data.db.entity.Stranka

//Relation 1 : many
data class SlucajSaStrankama(
    @Embedded val slucaj: Slucaj,
    @Relation(
        parentColumn = "id",
        entity = Stranka::class,
        entityColumn = "slucajId"
    )
    val stranke: List<Stranka>
) {
}