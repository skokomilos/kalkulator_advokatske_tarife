package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "vrsta_parnice")
class VrstaParnice(@PrimaryKey
                   @NotNull
                   val id: Long,
                   @NotNull
                   val naziv_vrste_parnice: String){

    override fun toString(): String {
        return naziv_vrste_parnice
    }
}