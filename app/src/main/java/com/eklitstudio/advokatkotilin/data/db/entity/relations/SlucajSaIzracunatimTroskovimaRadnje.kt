package com.eklitstudio.advokatkotilin.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.eklitstudio.advokatkotilin.data.db.entity.IzracunatTrosakRadnje
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj

data class SlucajSaIzracunatimTroskovimaRadnje(
    @Embedded val slucaj: Slucaj,
    @Relation(
        parentColumn = "id",
        entity = IzracunatTrosakRadnje::class,
        entityColumn = "slucaj_id"
    )
    val izracunatiTroskoviRadnja: List<IzracunatTrosakRadnje>
)