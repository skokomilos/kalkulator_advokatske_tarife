package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "tarifa_table"
)
data class Tarifa(
    @PrimaryKey(autoGenerate = false)
    @NotNull
    val idTarifa: Int,
    @NotNull
    val nazivTarife: String,
    val idPostupak: Long?,
    val idVrstaParnice: Long?){
}