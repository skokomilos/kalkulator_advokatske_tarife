package com.eklitstudio.advokatkotilin.ui.dodavanje_radnje

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.eklitstudio.advokatkotilin.EventObserver
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.relations.TarifaSaRadnjama
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.utilities.*
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeRadnjiViewModel
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.groupiex.plusAssign
import kotlinx.android.synthetic.main.dodavanje_radnje_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class DodavanjeRadnjeFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeRadnjiViewModel

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var groupLayoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupAdapter.apply {
            setOnItemClickListener(onItemClickListener)
            spanCount = 11
        }

        groupLayoutManager = GridLayoutManager(requireContext(), groupAdapter.spanCount).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }


        recycler_view.apply {
            layoutManager = groupLayoutManager
            adapter = groupAdapter
        }

        fab_dodavanje_radnje.setOnClickListener{
            Navigation.findNavController(it).navigate(
                DodavanjeRadnjeFragmentDirections
                    .actionDodavanjeRadnjeFragmentToSveDodateRadnjeIvrednostFragment(viewModel._slucajID.value!!)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(DodavanjeRadnjiViewModel::class.java)
        if(arguments != null) {
            var args = DodavanjeRadnjeFragmentArgs.fromBundle(arguments!!)
            viewModel.setSlucajid(args.idSlucaja)
            viewModel.setPostupakid(args.idPostupak)
        }

        return inflater.inflate(R.layout.dodavanje_radnje_fragment, container, false)

    }

    /**
     * Ovaj counter je ubacen jer na povratku na ovaj fragment kad pritisnem back
     * u listi se dupliraju tarife i radnje, tako da ovaj broj vrsi potvrdu da li je ista dodato u listu
     * //todo probaj da nadjes bolje resenje
     */
    var counter = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(counter == 0){
            counter = 1
            bindUI()
        }
        setupSnackbar()

    }

    private fun setupSnackbar(){
        viewModel.taskInsertRadnja.observe(viewLifecycleOwner, EventObserver{ nazivRadnje ->

            val snackbar = Snackbar
                .make(requireView(), "Dodato: \n$nazivRadnje", Snackbar.LENGTH_LONG)
            snackbar.show()
        })
    }


    fun bindUI(){
        launch(Dispatchers.Main){
            //slucaj sa strankama treba da bude prikazan kad se pritisne neki od buttona u toolbaru
            val tarifeZaPostupak = viewModel.tarifeZaPostupak.await()
            populateExpandableListView(tarifeZaPostupak)
        }
    }

    fun populateExpandableListView(list: List<TarifaSaRadnjama>){
        //listaSlucaja
        for(i in 0..list.size.minus(1)) {
            //naziv slucaj[i]
            val expandableHeaderItem = ExpandableHeaderItem(list.get(i).tarifa)
            groupAdapter += ExpandableGroup(expandableHeaderItem).apply {
                for (j in 0..list.get(i).radnje.size.minus(1)) {
                    //naziv stranke[i]
                    add(RadnjaItem(list.get(i).radnje.get(j)))
                }
            }
        }
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is RadnjaItem && !TextUtils.isEmpty(item.radnja.naziv_radnje)) {
            when(item.radnja.sifra)
            {
                CELA_VREDNOST ->  celaVrednosti(item.radnja.naziv_radnje.toString())
                POLOVINA_VREDNOSTI ->  polaVrednosti(item.radnja.naziv_radnje.toString())
                DUPLA_VREDNOST ->  duplaVrednosti(item.radnja.naziv_radnje.toString())
                CELA_VREDNOST_PLUS_SATI_MOZE_BITI_OTKAZANO_SA_OPCIJAMA_OTKAZANO_ODRZANO ->  celaVrednostPlusSatiSaOpcijomOdrzanoOtkazano(item.radnja.naziv_radnje.toString())
                CELA_VREDNOST_PLUS_SATI ->  celaVrednostPlusSati(item.radnja.naziv_radnje.toString())
                POLA_VREDNOST_PLUS_SATI ->  polaVrednostiPlusSati(item.radnja.naziv_radnje.toString())
                VREDNOST_KOJA_SE_UNOSI ->  dialogZaUnosNovca(item.radnja.naziv_radnje.toString())
                VREDNOST_SAMO_SATI ->  dialogZaUnosSati(item.radnja.naziv_radnje.toString())
            }
        }
    }

    fun celaVrednosti(nazivRadnje: String){

        launch(Dispatchers.Main) {
            var cena = viewModel.celaVrednost()
            dialogOsnovni(nazivRadnje, cena)
        }
    }

    fun polaVrednosti(nazivRadnje: String){
        launch(Dispatchers.Main){
            var cena = viewModel.polaVrednosti()
            dialogOsnovni(nazivRadnje, cena)
        }
            //ubaci dialog
    }

    fun duplaVrednosti(nazivRadnje: String){
        launch(Dispatchers.Main){
            var cena = viewModel.duplaVrednosti()
            dialogOsnovni(nazivRadnje, cena)
        }
    }

    private fun celaVrednostPlusSati(nazivRadnje: String) {
        launch(Dispatchers.Main){
            var cena = viewModel.celaVrednost()
            dialogZaUnosSatiPlusIzracunataCena(nazivRadnje, cena)
        }
    }


    private fun polaVrednostiPlusSati(nazivRadnje: String) {
        launch(Dispatchers.Main) {
            var cena = viewModel.celaVrednost()
            dialogZaUnosSatiPlusIzracunataCena(nazivRadnje.toString(), cena)
        }
    }



    fun celaVrednostPlusSatiSaOpcijomOdrzanoOtkazano(nazivRadnje: String){
        launch(Dispatchers.Main){
            var cena = viewModel.celaVrednost()
            dialogZaUnosSatiSaOpcijomOtkazano(nazivRadnje, cena)
        }
        //dodaj dialog
    }


    private var debugMode = false
    fun dialogOsnovni(nazivRadnje: String, cena : Double){
        MaterialDialog(requireContext()).show {
            title(text = nazivRadnje)
            message(text = " Cena ove radnje je ${cena.toString()} dinara\n da li zelite da je dodate?" )
            positiveButton(text = "da"){
                viewModel.insertRadnju(nazivRadnje, cena)
            }
            negativeButton(text = "ne")
            debugMode(debugMode)
            //lifecycleOwner(this@DodavanjeRadnjeFragment)
        }
    }

    fun dialogZaUnosNovca(nazivRadnje: String){
        MaterialDialog(requireContext()).show {
            title(text = nazivRadnje)
            input(
                hint = "Type something",
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                maxLength = 8
            ) { _, text ->
                viewModel.insertRadnju(nazivRadnje, text.toString().toDouble())
            }
            positiveButton(text = "da")
            negativeButton(text = "ne")
            debugMode(debugMode)
            //lifecycleOwner(this@MainActivity)
        }
    }

    fun dialogZaUnosSati(nazivRadnje: String){
        MaterialDialog(requireContext()).show {
            title(text = nazivRadnje)
            listItemsSingleChoice(R.array.brojSati, initialSelection = 2) { _, index, _ ->
                var izracunataCenaSaSatima: Double = (index * VREDNOST_JEDNOG_SATA * 30).toDouble()
                viewModel.insertRadnju(nazivRadnje, izracunataCenaSaSatima)
            }
            positiveButton(text = "dodaj")
            negativeButton(text = "ne")
            debugMode(debugMode)
            //lifecycleOwner(this@MainActivity)
        }
    }

    fun dialogZaUnosSatiPlusIzracunataCena(nazivRadnje: String, cena: Double){
        MaterialDialog(requireContext()).show {
            title(text = nazivRadnje)
            listItemsSingleChoice(R.array.brojSati, initialSelection = 2) { _, index, _ ->
                var izracunataCenaSaSatima = cena + (index * VREDNOST_JEDNOG_SATA * 30)
                viewModel.insertRadnju(nazivRadnje, izracunataCenaSaSatima)
            }
            positiveButton(text = "dodaj")
            negativeButton(text = "ne")
            debugMode(debugMode)
            //lifecycleOwner(this@MainActivity)
        }
    }


    fun dialogZaUnosSatiSaOpcijomOtkazano(nazivRadnje: String, cena: Double) {
        MaterialDialog(requireContext()).show {
            title(text = nazivRadnje)
            listItemsSingleChoice(R.array.brojSatiSaOtkazanim, initialSelection = 2) { _, index, _ ->
                var izracunataCenaSaSatima = cena + (index * VREDNOST_JEDNOG_SATA * 30)
                viewModel.insertRadnju(nazivRadnje, izracunataCenaSaSatima)
            }
            positiveButton(text = "dodaj")
            negativeButton(text = "otkazi")
            debugMode(debugMode)
            //lifecycleOwner(this@MainActivity)
        }
    }
}

