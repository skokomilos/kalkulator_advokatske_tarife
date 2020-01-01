package com.eklitstudio.advokatkotilin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postupak")
class Postupak(
               @PrimaryKey()
               val id: Long,
               val naziv_postupka: String) {

    override fun toString(): String {
        return naziv_postupka
    }
}
