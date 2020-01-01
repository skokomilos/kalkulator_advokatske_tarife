package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred

class KreirajPDFViewModel(private val repository: DodajSlucajRepository) : BaseViewModel()  {

    var _slucajID = MutableLiveData<String>()


    fun setSlucajId(idSlucaja: String) {
        _slucajID.value = idSlucaja
    }

    val slucajSaStrankama by lazyDeferred{
        repository.getSlucajSaStrankama(_slucajID.value!!)
    }

    val slucajSaIzracunatimTroskovimaRadnje by lazyDeferred {
        repository.getSlucajSaIzracunatimTroskovimaRadnja(_slucajID.value!!)
    }



}