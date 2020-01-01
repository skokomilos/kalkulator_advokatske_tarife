package com.eklitstudio.advokatkotilin.data.db.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.eklitstudio.advokatkotilin.data.db.entity.Radnja
import com.eklitstudio.advokatkotilin.data.db.entity.Tarifa

//Relation many to many

data class TarifaSaRadnjama(
    @Embedded val tarifa: Tarifa,
    @Relation(
        parentColumn = "idTarifa",
        entity = Radnja::class,
        entityColumn = "idRadnja",
        associateBy = Junction(
            value = TarifaRadnjaJoin::class,
            parentColumn = "idT",
            entityColumn = "idR"
        )
    )
    var radnje: List<Radnja>
)