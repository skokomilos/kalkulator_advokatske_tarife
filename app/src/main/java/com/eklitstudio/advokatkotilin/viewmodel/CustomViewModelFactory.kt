package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class CustomViewModelFactory constructor(
    private val dodajSlucajRepository: DodajSlucajRepository
): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when{
                isAssignableFrom(TraziSlucajViewModel::class.java) ->
                    TraziSlucajViewModel(
                        dodajSlucajRepository
                    )
                isAssignableFrom(DodavanjeSlucajaViewModel::class.java) ->
                    DodavanjeSlucajaViewModel(
                        dodajSlucajRepository
                    )
                isAssignableFrom(DodavanjeSlucajaUBazuViewModel::class.java) ->
                    DodavanjeSlucajaUBazuViewModel(
                        dodajSlucajRepository
                    )
                isAssignableFrom(DodavanjeRadnjiViewModel::class.java) ->
                    DodavanjeRadnjiViewModel(
                        dodajSlucajRepository
                    )
                isAssignableFrom(SveDodateRadnjeiNJihoveVrednostiViewModel::class.java) ->
                    SveDodateRadnjeiNJihoveVrednostiViewModel(
                        dodajSlucajRepository
                    )
                isAssignableFrom(KreirajPDFViewModel::class.java) ->
                    KreirajPDFViewModel(
                        dodajSlucajRepository
                    )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }

        } as T
}