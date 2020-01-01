package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eklitstudio.advokatkotilin.Event
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred

class SveDodateRadnjeiNJihoveVrednostiViewModel(repository: DodajSlucajRepository): BaseViewModel() {

    var _slucajID = MutableLiveData<String>()

    private val _kreirajPDFEvent = MutableLiveData<Event<Long>>()
    val kreirajPDFEvent : LiveData<Event<Long>> = _kreirajPDFEvent

    val slucaj by lazyDeferred{
        repository.getSlucajByID(_slucajID.value!!)
    }

    fun kreirajPDF(){
        _kreirajPDFEvent.value = Event(_slucajID.value!!.toLong())
    }

    fun setSlucajID(slucajID: String){
        _slucajID.value = slucajID
    }

    val slucajSaStrankama by lazyDeferred{
        repository.getSlucajSaStrankama(_slucajID.value!!)
    }

    val _slucajSaIzracunatimTroskovimaRadnje by lazyDeferred {
        repository._getSlucajSaIzracunatimTroskovimaRadnja(_slucajID.value!!) }

    val slucajSaIzracunatimTroskovimaRadnje by lazyDeferred {
        repository.getSlucajSaIzracunatimTroskovimaRadnja(_slucajID.value!!) }


}