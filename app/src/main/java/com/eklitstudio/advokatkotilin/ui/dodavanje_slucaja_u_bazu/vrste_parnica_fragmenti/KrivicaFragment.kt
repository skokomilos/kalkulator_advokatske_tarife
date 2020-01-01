package com.eklitstudio.advokatkotilin.ui.dodavanje_slucaja_u_bazu.vrste_parnica_fragmenti


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.TabelaBodova
import com.eklitstudio.advokatkotilin.databinding.FragmentKrivicaBinding
import com.eklitstudio.advokatkotilin.ui.base.ScopedFragment
import com.eklitstudio.advokatkotilin.viewmodel.CustomViewModelFactory
import com.eklitstudio.advokatkotilin.viewmodel.DodavanjeSlucajaUBazuViewModel
import kotlinx.android.synthetic.main.fragment_krivica.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class KrivicaFragment : ScopedFragment(), KodeinAware, AdapterView.OnItemSelectedListener  {

    override val kodein by closestKodein()
    private lateinit var binding: FragmentKrivicaBinding
    private val viewModelFactory: CustomViewModelFactory by instance()
    private lateinit var viewModel: DodavanjeSlucajaUBazuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_krivica,
            container,
            false
        )
        viewModel = ViewModelProvider(parentFragment!!, viewModelFactory)
            .get(DodavanjeSlucajaUBazuViewModel::class.java)

        binding.viewModel = viewModel

        bindUi()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel._okrivljenBoolen.observe(viewLifecycleOwner, Observer { rezultat ->
            binding.krivicaRadioButtonPunomocjeId.isEnabled = rezultat
            binding.krivicaRadioButtonSluzbenaduznostId.isEnabled = rezultat
        })
    }

    private fun bindUi() = launch(Dispatchers.Main) {
        val zaprecenjeKazne = viewModel.unikatneVrednostiIzTabeleBodovaZaKrivicuIPrekrsaj.await()

        krivica_spinner_id!!.setOnItemSelectedListener(this@KrivicaFragment)
        val aa = ArrayAdapter(requireContext(), R.layout.custom_spinner, zaprecenjeKazne)
        aa.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        krivica_spinner_id!!.setAdapter(aa)

    }

    //spinner implemented method
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //spinner implemented method
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val idTabeleBodova = parent!!.getItemAtPosition(position) as TabelaBodova
        viewModel.setTabelaBodovaId(idTabeleBodova.bodovi)
        //Log.d("evo ", " pojeni ${idTabeleBodova.tarifni_uslov}")
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
