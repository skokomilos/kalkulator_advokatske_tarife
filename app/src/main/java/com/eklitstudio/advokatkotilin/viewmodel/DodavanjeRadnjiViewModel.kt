package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eklitstudio.advokatkotilin.Event
import com.eklitstudio.advokatkotilin.data.db.entity.IzracunatTrosakRadnje
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DodavanjeRadnjiViewModel(private val dodajSlucajRepository: DodajSlucajRepository) : BaseViewModel() {

    var _slucajID = MutableLiveData<String>()
    var _postupakID = MutableLiveData<Long>()

    private val _taskInsertRadnja = MutableLiveData<Event<String>>()
    val taskInsertRadnja : LiveData<Event<String>> = _taskInsertRadnja

    val slucaj by lazyDeferred {
        dodajSlucajRepository.getSlucajByID(_slucajID.value!!)
    }

    fun setSlucajid(slucajID: String){
        _slucajID.value = slucajID
    }

    fun setPostupakid(postupakID: Long){
        _postupakID.value = postupakID
    }

    private fun getNowDate(): String {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(c.time)
    }

    //postive button in view
    fun insertRadnju(nazivRadnje: String, cena : Double){
        //iz view naziv i cena
        //viewmodel datum, slucajId
        launch(Dispatchers.IO) {
            createIzvrsenaRadnja(
                IzracunatTrosakRadnje(
                    naziv_radnje = nazivRadnje,
                    datum = getNowDate(),
                    cena = cena,
                    slucaj_id = _slucajID.value.toString()
                )
            )
        }
    }

    private fun createIzvrsenaRadnja(izracunatTrosakRadnje: IzracunatTrosakRadnje) = viewModelScope.launch {
        dodajSlucajRepository.insertIzvrsenaRadnja(izracunatTrosakRadnje)
        _taskInsertRadnja.value = Event(izracunatTrosakRadnje.naziv_radnje.toString())
    }

    suspend fun celaVrednost(): Double {
        var slucaj_OBJ = slucaj.await()
        var bodovi = slucaj_OBJ.tabela_bodova
        return (bodovi + ((bodovi / 2) * (slucaj_OBJ.broj_stranaka) -1) * 30).toDouble()
    }

    suspend fun polaVrednosti(): Double {
        var slucaj_OBJ = slucaj.await()
        var bodovi = slucaj_OBJ.tabela_bodova.toDouble()
        return (bodovi + ((bodovi / 2) * (slucaj_OBJ.broj_stranaka) -1) * 30) / 2
    }

    suspend fun duplaVrednosti(): Double {
        var slucaj_OBJ = slucaj.await()
        var bodovi = slucaj_OBJ.tabela_bodova.toDouble()
        return (bodovi + ((bodovi / 2) * (slucaj_OBJ.broj_stranaka) -1) * 30) * 2
    }



    val slucajSaStrankama by lazyDeferred{
        dodajSlucajRepository.getSlucajSaStrankama(_slucajID.value!!)
    }

    val tarifeZaPostupak by lazyDeferred{
        dodajSlucajRepository.getTarifeZaPostupak(_postupakID.value!!.toLong())
    }
}