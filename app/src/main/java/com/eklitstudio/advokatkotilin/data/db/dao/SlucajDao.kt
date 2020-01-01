package com.eklitstudio.advokatkotilin.data.db.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.data.db.entity.Stranka
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaIzracunatimTroskovimaRadnje
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaStrankama

@Dao
abstract class SlucajDao{

    @Query("SELECT * FROM slucaj")
    abstract fun getSviSlucajevi() : LiveData<List<Slucaj>>

    @Query("SELECT * FROM slucaj WHERE id=:id")
    abstract fun getSlucajByID(id: String) : Slucaj


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSlucaj(slucaj: Slucaj): Long


    fun upsert(entity: Slucaj): Long{
        var id = insertSlucaj(entity)
        if (id == -1L){
            Log.d("greska", "greska")
        }

        return id
    }

    @Update
    abstract fun insertStranke(stranke: List<Stranka>)

    @Transaction
    @Query("SELECT * FROM slucaj  WHERE id=:slucajId")
    abstract fun getSlucajSaStrankama(slucajId: String): SlucajSaStrankama

    @Transaction
    @Query("SELECT * FROM slucaj WHERE id =:id")
    abstract fun _getSlucajSaIzracunatimTroskovimaRadnje(id: String): LiveData<SlucajSaIzracunatimTroskovimaRadnje>

    @Transaction
    @Query("SELECT * FROM slucaj WHERE id =:id")
    abstract fun getSlucajSaIzracunatimTroskovimaRadnje(id: String): SlucajSaIzracunatimTroskovimaRadnje



    fun insertStrankeZaSlucaj(slucajId: String, stranke: List<Stranka>){
        for (stranka in stranke){
            stranka.slucajId = slucajId
        }
        insertStranke(stranke)
    }

    @Query("SELECT * FROM slucaj WHERE sifra_slucaja=:sifraslucaja")
    abstract fun getSlucajBySifra(sifraslucaja: Int): Slucaj

}
