package com.eklitstudio.advokatkotilin.data.db.entity.relations

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(tableName = "tarifa_radnja_join",
    primaryKeys = ["idT", "idR"])
 data class TarifaRadnjaJoin (

    @NotNull
    @ColumnInfo(name = "idT")
    val idT: Long,
    @NotNull
    @ColumnInfo(name = "idR")
    val idR: Long){
 }