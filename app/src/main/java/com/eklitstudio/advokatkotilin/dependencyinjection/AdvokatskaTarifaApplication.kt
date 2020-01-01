package com.eklitstudio.advokatkotilin.dependencyinjection

import android.app.Application
import com.eklitstudio.advokatkotilin.data.db.AdvokatDatabase
import com.eklitstudio.advokatkotilin.data.db.dao.*
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepositoryImpl
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AdvokatskaTarifaApplication:Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AdvokatskaTarifaApplication))

        bind() from singleton { AdvokatDatabase.getInstance(instance()) }
        bind<SlucajDao>() with singleton { instance<AdvokatDatabase>().slucajDao() }
        bind<PostupakDao>() with singleton { instance<AdvokatDatabase>().postupakDao() }
        bind<TarifaDao>() with singleton { instance<AdvokatDatabase>().tarifaDao() }
        bind<RadnjaDao>() with singleton { instance<AdvokatDatabase>().radnjaDao() }
        bind<TabelaBodovaDao>() with singleton { instance<AdvokatDatabase>().tabelaBodovaDao() }
        bind<StrankaDao>() with singleton { instance<AdvokatDatabase>().strankaDao() }
        bind<VrstaParniceDao>() with singleton { instance<AdvokatDatabase>().vrstaParniceDao() }
        bind<TarifaRadnjJoinDao>() with singleton { instance<AdvokatDatabase>().tarifaRadnjaDao()}
        bind<IzracunatTrosakRadnjeDao>() with singleton { instance<AdvokatDatabase>().izracunatTrosakRadnjeDao()}
        bind<DodajSlucajRepository>() with singleton { DodajSlucajRepositoryImpl(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
        bind() from provider{ CustomViewModelFactory(instance())}
    }

    override fun onCreate() {
        super.onCreate()
    }
}