package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.eklitstudio.advokatkotilin.data.db.entity.Tarifa
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaSaRadnjama

@Dao
interface TarifaDao {

    @Insert
    fun insertData(data: List<Tarifa>)

    @Query("SELECT * FROM tarifa_table")
    fun getSveTarife(): List<Tarifa>

    @Transaction
    @Query("SELECT * FROM tarifa_table WHERE idPostupak =:postupakId")
    suspend fun getTarifeZaPostupak(postupakId: Long): MutableList<TarifaSaRadnjama>
}