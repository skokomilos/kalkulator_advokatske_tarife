package com.eklitstudio.advokatkotilin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eklitstudio.advokatkotilin.data.db.dao.*
import com.eklitstudio.advokatkotilin.data.db.entity.*
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaRadnjaJoin


@Database(entities = [
    Slucaj::class,
    Postupak::class,
    Radnja::class,
    TabelaBodova::class,
    Tarifa::class,
    VrstaParnice::class,
    Stranka::class,
    TarifaRadnjaJoin::class,
    IzracunatTrosakRadnje::class],
    version = 1,
    exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class AdvokatDatabase: RoomDatabase(){


    companion object {

            private const val DATABASE_NAME = "novabaza"
            private const val DATABASE_DIR = "database/novabaza.db" // Asset/database/you_name.db

            fun getInstance(context: Context): AdvokatDatabase {
                return Room
                    .databaseBuilder(context, AdvokatDatabase::class.java, DATABASE_NAME)
                    .createFromAsset(DATABASE_DIR)
                    .build()
            }
    }

    /**
     * Connects the database to the DAO.
     */
    abstract fun slucajDao(): SlucajDao
    abstract fun postupakDao(): PostupakDao
    abstract fun vrstaParniceDao(): VrstaParniceDao
    abstract fun tarifaDao(): TarifaDao
    abstract fun radnjaDao(): RadnjaDao
    abstract fun tabelaBodovaDao(): TabelaBodovaDao
    abstract fun strankaDao(): StrankaDao
    abstract fun tarifaRadnjaDao(): TarifaRadnjJoinDao
    abstract fun izracunatTrosakRadnjeDao(): IzracunatTrosakRadnjeDao
}