package com.eklitstudio.advokatkotilin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eklitstudio.advokatkotilin.data.db.AdvokatDatabase
import com.eklitstudio.advokatkotilin.data.db.dao.SlucajDao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.StringBuilder

@RunWith(AndroidJUnit4::class)
class AdvokatDatabaseTest {

    private lateinit var slucajDao: SlucajDao
    private lateinit var db: AdvokatDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(
            context,
            AdvokatDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        slucajDao = db.slucajDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private var _slucajevi1 = MutableLiveData<List<Slucaj>>()
    private var listslucajeva = mutableListOf<Slucaj>()

    val slucajevi1 : LiveData<List<Slucaj>>
        get() = _slucajevi1



    @Test
    @Throws(Exception::class)
    fun insertAndGetSlucaj() {
        val slucaj = Slucaj("samsung")
        slucajDao.insert(slucaj)
        val getslucaj = slucajDao.getSlucaj()
        assertEquals(getslucaj?.nazivSlucaja, "samsung")
    }

    init {

    }

    @Test
    @Throws(Exception::class)
    fun getSlucajevi() {

        val stringbuilder = StringBuilder()
    }
}