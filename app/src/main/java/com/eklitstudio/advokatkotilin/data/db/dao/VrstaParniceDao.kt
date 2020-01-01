package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eklitstudio.advokatkotilin.data.db.entity.VrstaParnice

@Dao
interface VrstaParniceDao {

    @Insert
    fun insertData(data: List<VrstaParnice>)

    @Query("SELECT * FROM vrsta_parnice")
    fun getVrsteParnica(): List<VrstaParnice>
}