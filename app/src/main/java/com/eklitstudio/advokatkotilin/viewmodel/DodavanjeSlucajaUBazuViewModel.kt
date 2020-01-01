package com.eklitstudio.advokatkotilin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eklitstudio.advokatkotilin.Event
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak
import com.eklitstudio.advokatkotilin.data.db.entity.Slucaj
import com.eklitstudio.advokatkotilin.data.db.entity.Stranka
import com.eklitstudio.advokatkotilin.data.db.entity.TabelaBodova
import com.eklitstudio.advokatkotilin.data.db.repository.DodajSlucajRepository
import com.eklitstudio.advokatkotilin.internal.lazyDeferred
import com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.adapter.KlasaZaProgramabilnoDodavanjeEditTextova
import com.eklitstudio.advokatkotilin.utilities.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class DodavanjeSlucajaUBazuViewModel(private val dodajSlucajRepository: DodajSlucajRepository) : BaseViewModel() {

    val _listaStranaka = MutableLiveData<List<Stranka>>()
    val _dodavanjestranaka = MutableLiveData<List<KlasaZaProgramabilnoDodavanjeEditTextova>>()

    private val _sacuvajSlucajEvent = MutableLiveData<Event<String>>()
    val sacuvajSlucajEvent: LiveData<Event<String>> = _sacuvajSlucajEvent

    private var slucajId: String? = null

    val slucajIdLivedata = MutableLiveData<String>()

    val tabelaBodovaLista by lazyDeferred {
        dodajSlucajRepository.getTabelaBodova(1)
    }

    private val _slucaj = MutableLiveData<Slucaj>()
    val slucaj: LiveData<Slucaj> get() = _slucaj


    val stranka: List<Stranka>? = null


    val _sifraSlucaja = MutableLiveData<Int>()
    val _brojStranaka = MutableLiveData<Int>()
    /*
    Polja za Objekat Slucaj - foreigh fields (postupakId, tabledaBodovaId, vrstaOdbrane, okrivljen/ostecen)
    */
    var _postupakId = MutableLiveData<Long>()
    var _tabledaBodovaVrednosti = MutableLiveData<Int>()

    //ovi podaci se odnose na Krivica postupak
    val _okrivljen = MutableLiveData<Int>()

    //observe this u krivica fragment
    val _okrivljenBoolen = MutableLiveData<Boolean>()
    //ovi podaci se odnose na Krivicni postupak
    val _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti = MutableLiveData<Int>()

    init {
        _okrivljen.value = OKRIVLJEN
        _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value = VRSTA_ODBRANE_PUNOMOC
    }

    /*
    ova stavka se nalazi u postupku krivica
    @param boolean oznacava da li je okrivljen tacno ili ne
    okrivljen = true, u suprotnom okrivljen = false(to znaci da je ostecen)
     */
    fun jeliOkrivljen(boolean: Boolean) {
        //todo ovde cu verovatno trebati da resim problem ako nije krivica u pitanju, i da vidim koji broj da unesem za ovaj metod i za jelipunomoc
        if (boolean) {
            _okrivljen.value = OKRIVLJEN
            _okrivljenBoolen.value = boolean
            jeliPunomoc(punomocBoolesn)
        } else {
            _okrivljen.value = OSTECEN
            _okrivljenBoolen.value = boolean
            _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value = VRSTA_ODBRANE_PRAZNO
        }
    }

    var punomocBoolesn = false
    fun setPunomoc(boolean: Boolean) {
        punomocBoolesn = boolean
    }

    /*
    stavka koja se nalazi u postupku krivica
    @param boolean koji definisinise da li je upitanju obrana po punomocju ili sluzbenoj duznosti
    punomoc = true, sluzbenaDuznost = false
     */
    fun jeliPunomoc(boolean: Boolean) {
        if (boolean) {
            _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value = VRSTA_ODBRANE_PUNOMOC
        } else
            _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value = VRSTA_ODBRANE_SLUZBENA_DUZNOST
    }

    //onClick on fab. (fragment_dodavanje_slucaja_u_bazu.xml
    fun dodajSlucaj() {
        val novislucajID = slucajId
        Timber.d("timber sifra ${_sifraSlucaja.value} brojstranak ${_brojStranaka.value} okrivljen ${_okrivljen.value} vrstaodbrane ${_vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value} tabelabodova ${_tabledaBodovaVrednosti.value} i jos postupak ${_postupakId.value}")
        if(novislucajID == null) {
            kreirajSlucaj(
                Slucaj(
                    sifra_slucaja = _sifraSlucaja.value!!.toInt(),
                    broj_stranaka = _brojStranaka.value!!.toInt(),
                    okrivljen_ostecen = _okrivljen.value!!.toInt(),
                    vrsta_odbrane = _vrstaOdbrane_PoPunumocjuIliSluzbenojduznosti.value!!.toInt(),
                    tabela_bodova = _tabledaBodovaVrednosti.value!!.toLong(),
                    postupak_id = _postupakId.value!!.toLong()
                ), _listaStranaka.value!!
            )
        }
    }

    /**
     * Gresku koju prijavljujem preko snackbara u Fragmentu
     */
    private val _greskaPriDodavanjuSlucajaTask = MutableLiveData<Event<Boolean>>()
    val greskaPriDodavanjuSlucajaTask : LiveData<Event<Boolean>> = _greskaPriDodavanjuSlucajaTask


    private fun  kreirajSlucaj(slucaj: Slucaj, lista: List<Stranka>){
        launch(Dispatchers.Main){

            var isvalid = dodajSlucajRepository.insertSlucaj(slucaj)
            if(isvalid == -1L){
                val errorMsg = true
                _greskaPriDodavanjuSlucajaTask.value = Event(errorMsg)
            }else{
                var slcID = getSlucajId().id
                if(slcID != ""){
                    dodajSlucajRepository.insertStrankeZaDatiSlucaj(slcID, lista)
                    _sacuvajSlucajEvent.value = Event(slcID)
                }
            }
        }
    }


    private suspend fun  getSlucajId(): Slucaj{
        return withContext(IO){
           dodajSlucajRepository.getSlucajBySifra(_sifraSlucaja.value!!.toInt())
        }
    }

       fun getPostupakIzPretrage(long: Long): Deferred <Postupak> {
           val postupak by lazyDeferred{
               dodajSlucajRepository.getPostupakById(long)
           }
           return postupak
       }

     suspend  fun setPostupak (long : Long): String {
//         var postupak : Postupak?= null
//          CoroutineScope(IO).launch {
//             postupak = getPostupakIzPretrage(long).await()
//             Log.d("dosada set postupak", postupak!!.naziv_postupka)
//         }

         return withContext(IO){
             getPostupakIzPretrage(long).await().naziv_postupka
         }
    }

    /*
    Metod se poziva iz Fragmenta (fragtment : parnica, krivica, prekrsaj..)
    */
    fun setTabelaBodovaId(tabledaBodovaId: Int){
        _tabledaBodovaVrednosti.value = tabledaBodovaId
    }

    //poziva se iz DodavanjeSlucajaUBazuFragment
    //dodjeljujem vrednost iz safeArgs
    fun setPostupakId(long: Long){
        _postupakId.value = long
    }

    //poziva se iz DodavanjeSlucajaUBazuFragment
    //dodjeljujem vrednost iz safeArgs
    fun setLiveDataBrojStranaka(int: Int){
        _brojStranaka.value = int
    }

    //poziva se iz DodavanjeSlucajaUBazuFragment
    //dodjeljujem vrednost iz safeArgs
    fun setSifraSlucaja(int: Int){
       _sifraSlucaja.value = int
    }


    val postupak by lazyDeferred{
        dodajSlucajRepository.getPostupakById(_postupakId.value!!.toLong())
    }

    val unikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj by lazyDeferred{
        dodajSlucajRepository.getUnikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj(_postupakId.value!!.toLong())
    }

    val vrsteParnica by lazyDeferred{
        dodajSlucajRepository.getVrsteParnica()
    }

    suspend fun getVrednostiTabeleBodovaZaPostupkeKojiImajuOpcijeProcenjenoINeprocenjeno(vrstaParniceId: Long): List<TabelaBodova> {
        if(vrstaParniceId.equals(NEPROCENLJIVO)) {
            if (_postupakId.value!!.toInt().equals(VANPARNICNI_POSTUPAK) ||
                _postupakId.value!!.toInt().equals(UPRAVNI_POSTUPAK) ||
                _postupakId.value!!.toInt().equals(UPRAVNI_SPOROVI) ||
                _postupakId.value!!.toInt().equals(OSTALI_POSTUPCI)
            ) {
                return dodajSlucajRepository.getUnikatneNeprocenjiveVrednostiIzTabeleBodova(
                    vrstaParniceId,
                    _postupakId.value!!.toLong()
                )
            }else{
                return dodajSlucajRepository.getVrednostiKojeDeleVisePostupaka(vrstaParniceId)
            }
        }else
        return dodajSlucajRepository.getVrednostiKojeDeleVisePostupaka(vrstaParniceId)
    }

}
fun <T> MutableLiveData<List<T>>.add(item: T) {
    val updatedItems = this.value ?: emptyList()
    this.value = updatedItems + listOf(item) }

