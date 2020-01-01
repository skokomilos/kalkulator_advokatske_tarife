package com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.vrste_parnica_fragmenti


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.TabelaBodova
import com.eklitstudio.advokatkotilin.data.db.entity.VrstaParnice
import com.eklitstudio.advokatkotilin.databinding.FragmentParnicaBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeSlucajaUBazuViewModel
import kotlinx.android.synthetic.main.fragment_parnica.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ParnicaFragment : ScopedFragment(), KodeinAware, AdapterView.OnItemSelectedListener  {

    override val kodein by closestKodein()
    private lateinit var binding: FragmentParnicaBinding
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeSlucajaUBazuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_parnica,
            container,
            false
        )

        viewModel = ViewModelProvider(parentFragment!!, viewModelFactory)
            .get(DodavanjeSlucajaUBazuViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        bindUI()

        return binding.root
    }

    fun bindUI() = launch(Dispatchers.Main){
        val vrsteParnica = viewModel.vrsteParnica.await()

        spinner_parnica_vrste_id!!.setOnItemSelectedListener(this@ParnicaFragment)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, vrsteParnica)
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner_parnica_vrste_id.setAdapter(arrayAdapter)
        }


    fun updateTabelaBodovaSpinner(id: Long){
        launch(Dispatchers.Main) {
            var tabelaBodovaList = viewModel.getVrednostiTabeleBodovaZaPostupkeKojiImajuOpcijeProcenjenoINeprocenjeno(id)

            spinner_parnica_tabelabodova_id!!.setOnItemSelectedListener(this@ParnicaFragment)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, tabelaBodovaList)
            arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            arrayAdapter.notifyDataSetChanged()
            spinner_parnica_tabelabodova_id.setAdapter(arrayAdapter)
            //spinner_parnica_tabelabodova_id!!.setOnItemSelectedListener(this@ParnicaFragment)

        }

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if(parent == spinner_parnica_vrste_id) {
            val vrsta_parnice_selected_item_id = (parent!!.getItemAtPosition(position) as VrstaParnice).id.toLong()
            updateTabelaBodovaSpinner(vrsta_parnice_selected_item_id)
        }
        else if(parent == spinner_parnica_tabelabodova_id){
            val tb = parent!!.getItemAtPosition(position) as TabelaBodova
            viewModel.setTabelaBodovaId(tb.bodovi)
        }
    }
}
