package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eklitstudio.advokatkotilin.data.db.entity.Radnja

@Dao
interface RadnjaDao {

    @Insert
    fun insertData(data: List<Radnja>)

    @Query("SELECT * FROM radnja")
    fun getSveRadnje(): List<Radnja>
}
