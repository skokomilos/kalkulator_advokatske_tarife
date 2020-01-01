package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Update
import com.eklitstudio.advokatkotilin.data.db.entity.Stranka

@Dao
interface StrankaDao {

    @Update
    abstract fun insertStranke(stranke: List<Stranka>)
}