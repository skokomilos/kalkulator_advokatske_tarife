package com.eklitstudio.advokatkotilin.data.db.repository

import androidx.lifecycle.LiveData
import com.eklitstudio.advokatkotilin.data.db.entity.*
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaIzracunatimTroskovimaRadnje
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaStrankama
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaSaRadnjama

interface DodajSlucajRepository {

    suspend fun insertSlucaj(slucaj: Slucaj): Long

    suspend fun insertStrankeZaDatiSlucaj(slucajID: String, stranke: List<Stranka>?)

    suspend fun insertIzvrsenaRadnja(izracunatTrosakRadnje: IzracunatTrosakRadnje)

    suspend fun insertStranke(stranke: List<Stranka>)

    suspend fun getSlucajByID(id: String): Slucaj

    suspend fun getSlucajBySifra(sifraslucaja: Int): Slucaj

    suspend fun getSviSlucajevi(): LiveData<out List<Slucaj>>

    suspend fun getSlucajSaStrankama(slucajId: String): SlucajSaStrankama

    suspend fun _getSlucajSaIzracunatimTroskovimaRadnja(slucajId: String): LiveData<SlucajSaIzracunatimTroskovimaRadnje>

    suspend fun getSlucajSaIzracunatimTroskovimaRadnja(slucajId: String): SlucajSaIzracunatimTroskovimaRadnje

    suspend fun getSviPostupci(): List<Postupak>

    suspend fun getSveRadnje(): List<Radnja>

    suspend fun getSveTarife(): List<Tarifa>

    suspend fun getVrsteParnica(): List<VrstaParnice>

    suspend fun getTarifeZaPostupak(postupakId : Long): List<TarifaSaRadnjama>

    suspend fun getPostupakById(long: Long): Postupak

    suspend fun getTabelaBodova(postupakId: Long): List<TabelaBodova>

    suspend fun getUnikatneNeprocenjiveVrednostiIzTabeleBodova(vrstaParniceID: Long, postupakID: Long): List<TabelaBodova>

    suspend fun getVrednostiKojeDeleVisePostupaka(vrstaParniceID: Long): List<TabelaBodova>

    suspend fun getUnikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj(postupakId: Long): List<TabelaBodova>

}