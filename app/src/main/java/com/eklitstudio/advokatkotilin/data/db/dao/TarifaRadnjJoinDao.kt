package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaSaRadnjama

@Dao
interface TarifaRadnjJoinDao {

    @Transaction
    @Query("SELECT * FROM tarifa_table")
    suspend fun getTarifeZaPostupak(): MutableList<TarifaSaRadnjama>
}