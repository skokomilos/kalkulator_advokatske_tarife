package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.eklitstudio.advokatkotilin.data.db.entity.IzracunatTrosakRadnje

@Dao
interface IzracunatTrosakRadnjeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIzracunatTrosakRadnje(izracunatTrosakRadnje: IzracunatTrosakRadnje): Long
}