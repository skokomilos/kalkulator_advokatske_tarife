package com.eklitstudio.advokatkotilin.ui.odredjivanje_parnice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.Postupak
import com.eklitstudio.advokatkotilin.databinding.DodavanjeSlucajaFragmentBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeSlucajaViewModel
import kotlinx.android.synthetic.main.dodavanje_slucaja_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class OdredjivanjeParniceFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeSlucajaViewModel
    private lateinit var binding: DodavanjeSlucajaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            com.eklitstudio.advokatkotilin.R.layout.dodavanje_slucaja_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DodavanjeSlucajaViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonDodajSlucajId.setOnClickListener {

            if(viewModel.validateEditTextInputs()){
                val postupak : Postupak = spnr_dodajslucaj_id.selectedItem as Postupak

                Navigation.findNavController(it).navigate(
                    OdredjivanjeParniceFragmentDirections.actionDodajSlucajFragmentToDodavanjeSlucajaUBazu(
                        viewModel._sifraSlucaja.value.toString().toInt(), viewModel._brojStranaka.value.toString().toInt(), postupak.id
                    )
                )
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {

        val dummyData = viewModel.sviPostupci.await()

        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, dummyData)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spnr_dodajslucaj_id.adapter = adapter
    }
}