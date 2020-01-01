package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "radnja")
data class Radnja(
    @PrimaryKey(autoGenerate = true)
    val idRadnja: Long?,
    val naziv_radnje : String?,
    val sifra: Int?) {
}