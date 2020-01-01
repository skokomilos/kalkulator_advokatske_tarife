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
import com.eklitstudio.advokatkotilin.databinding.FragmentPrekrsajBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeSlucajaUBazuViewModel
import kotlinx.android.synthetic.main.fragment_prekrsaj.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PrekrsajFragment : ScopedFragment(), KodeinAware {


    override val kodein by closestKodein()
    private lateinit var binding: FragmentPrekrsajBinding
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeSlucajaUBazuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_prekrsaj,
            container,
            false
        )
        viewModel = ViewModelProvider(parentFragment!!, viewModelFactory)
            .get(DodavanjeSlucajaUBazuViewModel::class.java)

        binding.setLifecycleOwner(this)

        return binding.root
    }

    fun bindUI() = launch(Dispatchers.Main) {
        val podaciZaSpiner = viewModel.unikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj.await()

        spinner_prekrsaj_id?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val tabelaBodova = parent!!.getItemAtPosition(position) as TabelaBodova
                viewModel.setTabelaBodovaId(tabelaBodova.bodovi)
            }

        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, podaciZaSpiner)
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner_prekrsaj_id.setAdapter(arrayAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

}
