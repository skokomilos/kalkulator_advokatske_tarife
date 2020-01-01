package com.eklitstudio.advokatkotilin.data.db.repository

import androidx.lifecycle.LiveData
import com.eklitstudio.advokatkotilin.data.db.dao.*
import com.eklitstudio.advokatkotilin.data.db.entity.*
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaIzracunatimTroskovimaRadnje
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaStrankama
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaSaRadnjama
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DodajSlucajRepositoryImpl(
    private val slucajDao: SlucajDao,
    private val postupakDao: PostupakDao,
    private val radnjaDao: RadnjaDao,
    private val tarifaDao: TarifaDao,
    private val tabelaBodovaDao: TabelaBodovaDao,
    private val vrstaParniceDao: VrstaParniceDao,
    private val strankaDao: StrankaDao,
    private val tarifaRadnjaJoinDao: TarifaRadnjJoinDao,
    private val izracunatTrosakRadnjeDao: IzracunatTrosakRadnjeDao
) : DodajSlucajRepository {


    override suspend fun getPostupakById(long: Long): Postupak{

        return withContext(Dispatchers.IO){
            postupakDao.getPostupakById(long)
        }
    }

    override suspend fun getTarifeZaPostupak(postupakId: Long): List<TarifaSaRadnjama> {
        return withContext(Dispatchers.IO){
            tarifaDao.getTarifeZaPostupak(postupakId)
        }
    }

    override suspend fun getTabelaBodova(postupakId: Long): List<TabelaBodova> {
        return withContext(Dispatchers.IO){
            tabelaBodovaDao.getSviTabelaBodova(postupakId)
        }
    }

    override suspend fun getUnikatneNeprocenjiveVrednostiIzTabeleBodova(
        vrstaParniceID: Long,
        postupakID: Long
    ): List<TabelaBodova> {
        return withContext(Dispatchers.IO){
            tabelaBodovaDao.getUnikatneNeprocenjiveVrednostiIzTabeleBodova(vrstaParniceID, postupakID)
        }
    }

    override suspend fun getUnikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj(postupakId: Long): List<TabelaBodova> {
        return withContext(Dispatchers.IO){
            tabelaBodovaDao.getListPoPostupkuID(postupakId)
        }
    }

    override suspend fun getVrednostiKojeDeleVisePostupaka(vrstaParniceID: Long): List<TabelaBodova> {
        return withContext(Dispatchers.IO) {
            tabelaBodovaDao.getListPoVrstiParnicaID(vrstaParniceID)
        }
    }

    override suspend fun getSveTarife(): List<Tarifa> {
        return withContext(Dispatchers.IO){
            tarifaDao.getSveTarife()
        }
    }

    override suspend fun getVrsteParnica(): List<VrstaParnice> {
        return withContext(Dispatchers.IO){
            vrstaParniceDao.getVrsteParnica()
        }
    }

    override suspend fun getSveRadnje(): List<Radnja> {
        return withContext(Dispatchers.IO){
            radnjaDao.getSveRadnje()
        }
    }

    override suspend fun getSviPostupci(): List<Postupak> {
        return withContext((Dispatchers.IO)){
            postupakDao.getSviPostupci()
        }
    }

    override suspend fun insertSlucaj(slucaj: Slucaj): Long{
        return withContext(IO){
            slucajDao.upsert(slucaj)
        }
    }

    override suspend fun insertStrankeZaDatiSlucaj(slucajID: String, stranke : List<Stranka>?) {
        GlobalScope.launch(Dispatchers.IO) {

            slucajDao.insertStrankeZaSlucaj(slucajID, stranke!!)
        }
    }

    override suspend fun getSlucajByID(id: String): Slucaj {
        return withContext(IO){
            slucajDao.getSlucajByID(id)
        }
    }

    override suspend fun getSlucajBySifra(sifraslucaja: Int): Slucaj {
        return withContext(IO){
            slucajDao.getSlucajBySifra(sifraslucaja)
        }
    }

    override suspend fun getSviSlucajevi(): LiveData<List<Slucaj>> {
        return withContext(Dispatchers.IO) {
            slucajDao.getSviSlucajevi()
        }
    }

    override suspend fun getSlucajSaStrankama(slucajId: String): SlucajSaStrankama{

        return withContext(IO){
           slucajDao.getSlucajSaStrankama(slucajId)
        }
    }

    override suspend fun _getSlucajSaIzracunatimTroskovimaRadnja(slucajId: String): LiveData<SlucajSaIzracunatimTroskovimaRadnje>{
        return withContext(IO){
            slucajDao._getSlucajSaIzracunatimTroskovimaRadnje(slucajId)
        }
    }

    override suspend fun getSlucajSaIzracunatimTroskovimaRadnja(slucajId: String): SlucajSaIzracunatimTroskovimaRadnje{
        return withContext(IO){
            slucajDao.getSlucajSaIzracunatimTroskovimaRadnje(slucajId)
        }
    }

    override suspend fun insertIzvrsenaRadnja(izracunatTrosakRadnje: IzracunatTrosakRadnje) {
        GlobalScope.launch(IO){
            izracunatTrosakRadnjeDao.insertIzracunatTrosakRadnje(izracunatTrosakRadnje)
        }
    }

    override suspend fun insertStranke(stranke: List<Stranka>) {
        GlobalScope.launch(IO){
            strankaDao.insertStranke(stranke)
        }
    }
}