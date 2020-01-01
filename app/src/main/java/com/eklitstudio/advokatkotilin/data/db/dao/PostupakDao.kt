package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak

@Dao
interface PostupakDao {

    @Insert
    fun insertData(data: List<Postupak>)

    @Insert
    fun insert(postupak:Postupak)

    @Query("SELECT * FROM postupak")
    fun getSviPostupci(): List<Postupak>

    @Query("SELECT * FROM postupak WHERE id = :id LIMIT 1")
    suspend fun getPostupakById(id: Long): Postupak

}