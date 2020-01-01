package com.eklitstudio.advokatkotilin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eklitstudio.advokatkotilin.data.db.entity.TabelaBodova

@Dao
interface TabelaBodovaDao {

    @Insert
    fun insertData(data: List<TabelaBodova>)

    @Query("SELECT * FROM tabela_bodova WHERE postupak_id = :id")
    fun getSviTabelaBodova(id: Long) : List<TabelaBodova>

    @Query("SELECT * FROM tabela_bodova WHERE vrste_parnica_id =:vrstaParniceID AND postupak_id =:postupakID")
    fun getUnikatneNeprocenjiveVrednostiIzTabeleBodova(vrstaParniceID: Long, postupakID: Long): List<TabelaBodova>

    @Query("SELECT * FROM tabela_bodova WHERE postupak_id =:i")
    fun getUnikatneNeprocenjiveVrednostiIzTabeleBodova(i: Long): List<TabelaBodova>


    @Query("SELECT * FROM tabela_bodova WHERE postupak_id = :postupakID AND vrste_parnica_id IS NULL")
    fun getListPoPostupkuID(postupakID: Long): List<TabelaBodova>

    @Query("SELECT * FROM tabela_bodova WHERE vrste_parnica_id = :vrstaParniceID AND postupak_id IS NULL")
    fun getListPoVrstiParnicaID(vrstaParniceID: Long): List<TabelaBodova>

    @Query("SELECT * FROM tabela_bodova WHERE vrste_parnica_id =:vrstaParniceID AND postupak_id =:postupakID")
    fun getListPoPostupkuIDiVrstiParnicaID(postupakID: Long, vrstaParniceID: Long): List<TabelaBodova>
}
