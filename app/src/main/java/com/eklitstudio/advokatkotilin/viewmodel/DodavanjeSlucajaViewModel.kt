package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred
import com.eklitstudio.advokatkotilin.validator.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class DodavanjeSlucajaViewModel(
    private val dodajSlucajRepository:DodajSlucajRepository) : ViewModel() {

    private var viewModelJob = Job()

    var _sifraSlucaja = MutableLiveData<CharSequence>()
    var _brojStranaka = MutableLiveData<CharSequence>()
    var SelectedItem = MutableLiveData<Postupak>()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val sviPostupci by lazyDeferred{
        dodajSlucajRepository.getSviPostupci()
    }

    val errorSifraSlucajaFormat = MutableLiveData<String>()
    val errorBrojStranakaFormat = MutableLiveData<String>()


    fun setBrojStranaka(s : CharSequence){
        _brojStranaka.value = s
        Timber.d("timber ${_brojStranaka.value.toString()}")
    }

    fun setSifraSlucaja(char: CharSequence){
        _sifraSlucaja.value = char
    }

    fun validateEditTextInputs(): Boolean{

        var isValid = true
        if (_sifraSlucaja.value.toString().isEmpty() || _sifraSlucaja.value == null || !Validator.isNumberValid(_sifraSlucaja.value.toString())){
            errorSifraSlucajaFormat.postValue("Invalid username!")
            isValid = false
        }else{
            errorSifraSlucajaFormat.postValue(null)
        }

        if(_brojStranaka.value.toString().isEmpty() ||  _brojStranaka.value == null || !Validator.isNumberValid(_brojStranaka.value.toString())){
            errorBrojStranakaFormat.postValue("Invalid email!")
            isValid = false
        }else{
            errorBrojStranakaFormat.postValue(null)
        }

        return isValid
    }
}


