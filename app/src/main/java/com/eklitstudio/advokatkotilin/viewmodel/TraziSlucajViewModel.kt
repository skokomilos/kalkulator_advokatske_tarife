package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class TraziSlucajViewModel(private val dodajSlucajRepository: DodajSlucajRepository) : ViewModel() {

    private var viewModelJob = Job()
    var score = MutableLiveData<Slucaj>()
    var postupak = MutableLiveData<Postupak>()


    private val uiScopre = CoroutineScope(Dispatchers.Main + viewModelJob)

    val postupci by lazyDeferred{
        dodajSlucajRepository.getSviPostupci()
    }

    val tarife by lazyDeferred{
        dodajSlucajRepository.getSveTarife()
    }

    val slucajevi by lazyDeferred{
        dodajSlucajRepository.getSviSlucajevi()
    }

    val radnje by lazyDeferred{
        dodajSlucajRepository.getSveRadnje()
    }

    val tabelaBodova by lazyDeferred{
        dodajSlucajRepository.getTabelaBodova(postupak.value!!.id)
    }

    val tarifeByPostupak by lazyDeferred{
        //dodajSlucajRepository.getTarifeZaPostupak(2)
    }



}
