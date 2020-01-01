package com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eklitstudio.advokatkotilin.EventObserver
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.Stranka
import com.eklitstudio.advokatkotilin.databinding.FragmentDodavanjeSlucajaUBazuBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.adapter.KlasaZaProgramabilnoDodavanjeEditTextova
import com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.vrste_parnica_fragmenti.KrivicaFragment
import com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.vrste_parnica_fragmenti.ParnicaFragment
import com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.vrste_parnica_fragmenti.PrekrsajFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeSlucajaUBazuViewModel
import com.eklitstudio.advokatkotilin.viewmodel.add
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class DodavanjeSlucajaUBazuFragment: ScopedFragment(), KodeinAware{

    override val kodein by closestKodein()
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeSlucajaUBazuViewModel
    private lateinit var binding: FragmentDodavanjeSlucajaUBazuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dodavanje_slucaja_u_bazu,
            container,
            false)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DodavanjeSlucajaUBazuViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        if(arguments != null){
            var args = DodavanjeSlucajaUBazuFragmentArgs.fromBundle(arguments!!)
            var brojstranaka = args.brojStranaka
            var sifraslucaja = args.sifraSlucaja
            var postupakId = args.postupakId
            viewModel.setPostupakId(postupakId)
            viewModel.setLiveDataBrojStranaka(brojstranaka)
            viewModel.setSifraSlucaja(sifraslucaja)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragment()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupNavigation()
        greskaSnackBar()
    }

    private fun greskaSnackBar(){
        viewModel.greskaPriDodavanjuSlucajaTask.observe(viewLifecycleOwner, EventObserver{
            if(it == true) {
                val snackbar = Snackbar
                    .make(requireView(), R.string.greska_dodavanje_slucaj, Snackbar.LENGTH_LONG)
                snackbar.show()
            }})
    }

    fun setupFragment() = launch(Dispatchers.Main){

        val postupak = viewModel.postupak.await()

         var upperfragment : Fragment

                when(postupak.naziv_postupka){
                    "Krivični postupak" -> upperfragment = KrivicaFragment()
                    "Prekršajni postupak i postupak privrednih prestupa" -> upperfragment = PrekrsajFragment()
                    "Postupak pred ustavnim sudom" -> upperfragment = ParnicaFragment()
                    else -> upperfragment = ParnicaFragment()
        }


        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentcontainer, upperfragment)
            .commit()

        kreirajDonjiUI()

    }

    protected lateinit var dodavanjeEditTexta : KlasaZaProgramabilnoDodavanjeEditTextova
    protected lateinit var gridLayout: GridLayout

    fun kreirajDonjiUI(){

        for (i in 0 until viewModel._brojStranaka.value!!.toInt()) {

            dodavanjeEditTexta = KlasaZaProgramabilnoDodavanjeEditTextova()
            val stranka = Stranka("", "", "")

            val editTextIme: EditText = dodavanjeEditTexta.kreirajEditTextZaIme(context!!)
            val editTextAdresa: EditText = dodavanjeEditTexta.kreirajEditTextZaAdresu(context!!)
            val editTextMesto: EditText = dodavanjeEditTexta.kreirajEditTextZaMesto(context!!)

            viewModel._listaStranaka.add(stranka)

            gridLayout = binding.parnicaGridlayoutZaDinamickoDodavanjePolja
            gridLayout.addView(dodavanjeEditTexta.kreirajTextViewKojiCePreikazivatiBrojStranaka(context!!, (i + 1).toString()))

            //ovde kreiram 3 edittexta za ime, adresu, mesto
            gridLayout.addView(editTextIme)
            editTextIme.addTextChangedListener(object : TextWatcher {

                var redniBrojElementaUListi = i

                override fun afterTextChanged(s: Editable) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    viewModel._listaStranaka.value?.get(redniBrojElementaUListi)?.ime_i_prezime = s.toString()
                }
            })
            //adresa
            gridLayout.addView(editTextAdresa)
            editTextAdresa.addTextChangedListener(object : TextWatcher {

                var redniBrojElementaUListi = i

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    viewModel._listaStranaka.value?.get(redniBrojElementaUListi)?.adresa = s.toString()
                }
            })
            //mesto
            gridLayout.addView(editTextMesto)
            editTextMesto.addTextChangedListener(object : TextWatcher {

                var redniBrojElementaUListi = i

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    viewModel._listaStranaka.value?.get(redniBrojElementaUListi)?.mesto = s.toString()
                }
            })
        }
    }

    fun getClientName(redni_broj_edit_texta_u_grid_layout: Int): String {

        val imeiprezime = gridLayout.getChildAt(redni_broj_edit_texta_u_grid_layout) as EditText
        return imeiprezime.text.toString()
    }

    fun getClientAddress(i: Int): String {

        val adresa = gridLayout.getChildAt(i) as EditText
        return adresa.text.toString()
    }

    fun getClientPlace(i: Int): String {

        val mesto = gridLayout.getChildAt(i) as EditText
        return mesto.text.toString()
    }

    private fun setupNavigation() {
        viewModel.sacuvajSlucajEvent.observe(viewLifecycleOwner, EventObserver {idSlucaja ->
            val action = DodavanjeSlucajaUBazuFragmentDirections
                .actionDodavanjeSlucajaUBazuToDodavanjeRadnjeFragment(idSlucaja.toString(), viewModel._postupakId.value!!.toLong())
            findNavController().navigate(action)

            val snackbar = Snackbar
                .make(requireView(), R.string.slucaj_dodat, Snackbar.LENGTH_LONG)
            snackbar.show()
        })
    }

}